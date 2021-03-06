package org.kohsuke.github;

import com.google.common.collect.Iterables;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Kohsuke Kawaguchi
 */
public class CommitTest extends AbstractGitHubWireMockTest {
    @Test // issue 152
    public void lastStatus() throws IOException {
        GHTag t = gitHub.getRepository("stapler/stapler").listTags().iterator().next();
        assertNotNull(t.getCommit().getLastStatus());
    }

    @Test // issue 230
    public void listFiles() throws Exception {
        GHRepository repo = gitHub.getRepository("stapler/stapler");
        PagedIterable<GHCommit> commits = repo.queryCommits().path("pom.xml").list();
        for (GHCommit commit : Iterables.limit(commits, 10)) {
            GHCommit expected = repo.getCommit(commit.getSHA1());
            assertEquals(expected.getFiles().size(), commit.getFiles().size());
        }
    }

    @Test // issue 737
    public void commitSignatureVerification() throws Exception {
        GHRepository repo = gitHub.getRepository("stapler/stapler");
        PagedIterable<GHCommit> commits = repo.queryCommits().path("pom.xml").list();
        for (GHCommit commit : Iterables.limit(commits, 10)) {
            GHCommit expected = repo.getCommit(commit.getSHA1());
            assertEquals(expected.getCommitShortInfo().getVerification().isVerified(),
                    commit.getCommitShortInfo().getVerification().isVerified());
            assertEquals(expected.getCommitShortInfo().getVerification().getReason(),
                    commit.getCommitShortInfo().getVerification().getReason());
            assertEquals(expected.getCommitShortInfo().getVerification().getSignature(),
                    commit.getCommitShortInfo().getVerification().getSignature());
            assertEquals(expected.getCommitShortInfo().getVerification().getPayload(),
                    commit.getCommitShortInfo().getVerification().getPayload());
        }
    }
}
