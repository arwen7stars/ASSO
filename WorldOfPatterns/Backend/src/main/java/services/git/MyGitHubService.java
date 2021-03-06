package services.git;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import utils.RestClient;
import utils.exceptions.SearchFailedException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

import static utils.Utils.*;

/**
 * Class to connect with GitHub
 */
public class MyGitHubService {
    private static final String SEARCH_URL = "https://api.github.com/search/code?q=";
    private static final String REPOSITORY = "+repo:";
    private static final String MARKDOWN = "markdown";
    private static final String HEAD_MASTER = "heads/master";

    private static final String ITEMS = "items";
    private static final String PATH = "path";

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd H:m:s");

    private String user;
    private String password;
    private String email;
    private String repositoryName;

    private GitHubClient client;
    private RepositoryService repositoryService;
    private ContentsService contentsService;
    private CommitService commitService;
    private DataService dataService;
    private MarkdownService markdownService;
    private Repository repository;

    /**
     * Constructor. Sets up the GitHub services
     * @param user GitHub user
     * @param password GitHub password
     * @param email GitHub email
     * @param repositoryName GitHub repository name
     */
    public MyGitHubService(String user, String password, String email, String repositoryName) {
        this.user = user;
        this.password = password;
        this.email = email;
        this.repositoryName = repositoryName;

        client = new GitHubClient();
        client.setCredentials(user, password);

        commitService = new CommitService(client);
        dataService = new DataService(client);
        markdownService = new MarkdownService(client);

        repositoryService = new RepositoryService(client);
        contentsService = new ContentsService(client);

        try {
            repository = repositoryService.getRepository(user, repositoryName);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

    /**
     * Get the repository contents
     * @param path Path to get contents from
     * @return List of file names inside the directory at path
     * @throws IOException When the path is invalid
     */
    public ArrayList<GitFileBasicInfo> getRepositoryContents(String path) throws IOException {
        ArrayList<GitFileBasicInfo> res = new ArrayList<>();

        List<RepositoryContents> contents = contentsService.getContents(repository, path);

        for(RepositoryContents c: contents) {
            String id = c.getName();

            if(id.contains(POINT))
                continue;

            try {
                List<RepositoryContents> subcontents = contentsService.getContents(repository, path + id + SEPARATOR);

                for(RepositoryContents subc: subcontents) {
                    String name = subc.getName();

                    List<CommitBasicInfo> info = getRepositoryCommits(path + id + SEPARATOR);
                    String date = info.get(0).getDate();

                    res.add(new GitFileBasicInfo(Integer.parseInt(id), name.replace(FILE_FORMAT, EMPTY_STRING), date));
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return res;
    }

    /**
     * Get file content
     * @param path Path to the file
     * @return The file content
     * @throws IOException When the path is invalid
     */
    public String getFileContent(String path) throws IOException {
        String res = null;

        List<RepositoryContents> contents = contentsService.getContents(repository, path);

        for(RepositoryContents c: contents) {
            res = new String(Base64.decodeBase64(c.getContent().getBytes()));
        }

        return res;
    }

    /**
     * Get HTML markup from given markdown
     * @param markdown Markdown to be translated
     * @return String containing HTML code
     * @throws IOException When the file cannot be converted to HTML
     */
    public String getHtmlFromMarkdown(String markdown) throws IOException{
        return markdownService.getHtml(markdown,MARKDOWN);
    }

    /**
     * Commit a file
     * @param path Path to commit the file to
     * @param content Content of the file
     * @param message Commit message
     * @return True if commit successful. False otherwise
     */
    public void commit(String path, String content, String message)  throws IOException {
        // get some sha's from current state in git
        String baseCommitSha = repositoryService.getBranches(repository).get(0).getCommit().getSha();
        RepositoryCommit baseCommit = commitService.getCommit(repository, baseCommitSha);
        String treeSha = baseCommit.getSha();

        // create new blob with data
        Blob blob = new Blob();
        blob.setContent(content).setEncoding(Blob.ENCODING_UTF8);
        String blob_sha = dataService.createBlob(repository, blob);
        Tree baseTree = dataService.getTree(repository, treeSha);

        // create new tree entry
        TreeEntry treeEntry = new TreeEntry();
        treeEntry.setPath(path);
        treeEntry.setMode(TreeEntry.MODE_BLOB);
        treeEntry.setType(TreeEntry.TYPE_BLOB);
        treeEntry.setSha(blob_sha);
        treeEntry.setSize(blob.getContent().length());
        Collection<TreeEntry> entries = new ArrayList<>();
        entries.add(treeEntry);
        Tree newTree = dataService.createTree(repository, entries, baseTree.getSha());

        // create commit
        Commit commit = new Commit();
        commit.setMessage(message);
        commit.setTree(newTree);

        CommitUser author = new CommitUser();
        author.setName(user);
        author.setEmail(email);
        Calendar now = Calendar.getInstance();
        author.setDate(now.getTime());
        commit.setAuthor(author);
        commit.setCommitter(author);

        List<Commit> listOfCommits = new ArrayList<>();
        listOfCommits.add(new Commit().setSha(baseCommitSha));
        commit.setParents(listOfCommits);
        Commit newCommit = dataService.createCommit(repository, commit);

        // create resource
        TypedResource commitResource = new TypedResource();
        commitResource.setSha(newCommit.getSha());
        commitResource.setType(TypedResource.TYPE_COMMIT);
        commitResource.setUrl(newCommit.getUrl());

        // get master reference and update it
        Reference reference = dataService.getReference(repository, HEAD_MASTER);
        reference.setObject(commitResource);
        dataService.editReference(repository, reference, true);
    }

    /**
     * Gets commits at a given path in the repository
     * @param path The path in the repository to parse commits in
     * @return List of Commits with message and date
     * @throws IOException When the path is invalid
     */
    public List<CommitBasicInfo> getRepositoryCommits(String path) throws IOException {
        ArrayList<CommitBasicInfo> res = new ArrayList<>();

        List<RepositoryCommit> commits = commitService.getCommits(repository, null, path);

        for(RepositoryCommit commit: commits) {
            Commit c = commit.getCommit();
            String message = c.getMessage();
            String date = dateFormatter.format(c.getAuthor().getDate());
            String sha = commit.getSha();
            res.add(new CommitBasicInfo(message, date, sha));
        }

        return res;
    }

    /**
     * Get an old revision of a file
     * @param path The path to the file
     * @param sha The sha of the revision
     * @return The old revision's content
     * @throws IOException When the revision does not exist or the path is invalid
     */
    public String getOldFileRevisionContent(String path, String sha) throws IOException {
        String res = null;

        List<RepositoryContents> contents = contentsService.getContents(repository, path, sha);

        for(RepositoryContents c: contents) {
            res = new String(Base64.decodeBase64(c.getContent().getBytes()));
        }

        return res;
    }

    /**
     * Search a repository based on a query
     * @param query The query to filter the search
     * @return A list of paths to files relevant to the search
     * @throws IOException When an error occurs
     */
    public ArrayList<String> searchRepository(String query) throws IOException {
        ArrayList<String> res = new ArrayList<>();

        ResponseEntity<String> response = RestClient.get(SEARCH_URL + query + REPOSITORY + user + SEPARATOR + repositoryName);

        if(response.getStatusCode() != HttpStatus.OK) {
            throw new SearchFailedException();
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.getBody());

        JsonNode items = actualObj.get(ITEMS);

        if (items.isArray()) {
            for (final JsonNode node : items) {
                String path = node.get(PATH).toString();

                res.add(path);
            }
        }

        return res;
    }
}
