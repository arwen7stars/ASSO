package services.git;

import org.apache.tomcat.util.codec.binary.Base64;
import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * Class to connect with GitHub
 */
public class MyGitHubService {
    private static final String MARKDOWN = "markdown";
    private static final String HEAD_MASTER = "heads/master";

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
     */
    public ArrayList<String> getRepositoryContents(String path) {
        ArrayList<String> res = new ArrayList<>();

        try {
            List<RepositoryContents> contents = contentsService.getContents(repository, path);

            for(RepositoryContents c: contents) {
                String name = c.getName();
                res.add(name);
            }
        }
        catch(IOException ex) {
            System.out.println(ex.getMessage());
        }

        return res;
    }

    /**
     * Get file content
     * @param path Path to the file
     * @return The file content
     */
    public String getFileContent(String path) {
        String res = null;

        try {
            List<RepositoryContents> contents = contentsService.getContents(repository, path);

            for(RepositoryContents c: contents) {
                res = new String(Base64.decodeBase64(c.getContent().getBytes()));
            }
        }
        catch(IOException ex) {
            System.out.println(ex.getMessage());
        }

        return res;
    }

    /**
     * Get HTML markup from given markdown
     * @param markdown Markdown to be translated
     * @return String containing HTML code
     */
    public String getHtmlFromMarkdown(String markdown) {
        String res = null;

        try {
            res = markdownService.getHtml(markdown,MARKDOWN);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return res;
    }

    /**
     * Commit a file
     * @param path Path to commit the file to
     * @param content Content of the file
     * @param message Commit message
     * @return True if commit successful. False otherwise
     */
    public boolean commit(String path, String content, String message) {
        boolean res = false;

        try {
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
            res = true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return res;
    }

    /**
     * Gets commits at a given path in the repository
     * @param path The path in the repository to parse commits in
     * @return List of Commits with message and date
     */
    public List<CommitBasicInfo> getRepositoryCommits(String path) throws IOException {
        ArrayList<CommitBasicInfo> res = new ArrayList<>();

        List<RepositoryCommit> commits = commitService.getCommits(repository, null, path);

        for(RepositoryCommit commit: commits) {
            Commit c = commit.getCommit();
            String message = c.getMessage();
            String date = dateFormatter.format(c.getAuthor().getDate());
            res.add(new CommitBasicInfo(message, date));
        }

        return res;
    }
}
