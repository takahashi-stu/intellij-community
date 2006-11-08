package com.intellij.localvcs;

import org.junit.Test;

public class RootEntryFilesTest extends TestCase {
  private RootEntry root = new RootEntry();

  @Test
  public void testCreatingFiles() {
    assertFalse(root.hasEntry(p("file")));

    root.doCreateFile(p("file"), "content");
    assertTrue(root.hasEntry(p("file")));

    Entry e = root.getEntry(p("file"));

    assertEquals(FileEntry.class, e.getClass());
    assertEquals("content", e.getContent());
  }

  @Test
  public void testCreatingTwoFiles() {
    root.doCreateFile(p("file1"), null);
    root.doCreateFile(p("file2"), null);

    assertTrue(root.hasEntry(p("file1")));
    assertTrue(root.hasEntry(p("file2")));

    assertFalse(root.hasEntry(p("unknown file")));
  }

  @Test
  public void testCreatingFileWithExistingNameThrowsException() {
    root.doCreateFile(p("file"), null);

    try {
      root.doCreateFile(p("file"), null);
      fail();
    } catch (LocalVcsException e) {}
  }

  @Test
  public void testChangingFileContent() {
    root.doCreateFile(p("file"), "content");
    root.doChangeFileContent(p("file"), "new content");

    assertEquals("new content", root.getEntry(p("file")).getContent());
  }

  @Test
  public void testChangingFileContentAffectsOnlyOneFile() {
    root.doCreateFile(p("file1"), "content1");
    root.doCreateFile(p("file2"), "content2");

    root.doChangeFileContent(p("file1"), "new content");

    assertEquals("new content", root.getEntry(p("file1")).getContent());
    assertEquals("content2", root.getEntry(p("file2")).getContent());
  }

  @Test
  public void testChangingContentOfAnNonExistingFileThrowsException() {
    try {
      root.doChangeFileContent(p("file"), "content");
      fail();
    } catch (LocalVcsException e) {}
  }

  @Test
  public void testRenamingFiles() {
    root.doCreateFile(p("file"), "content");

    root.doRename(p("file"), "new file");

    assertFalse(root.hasEntry(p("file")));
    assertTrue(root.hasEntry(p("new file")));

    assertEquals("content", root.getEntry(p("new file")).getContent());
  }

  @Test
  public void testRenamingOfAnNonExistingFileThrowsException() {
    try {
      root.doRename(p("file"), "new file");
      fail();
    } catch (LocalVcsException e) {}
  }

  @Test
  public void testRenamingFileToAnExistingFileNameThrowsException() {
    root.doCreateFile(p("file1"), null);
    root.doCreateFile(p("file2"), null);

    try {
      root.doRename(p("file1"), "file2");
      fail();
    } catch (LocalVcsException e) {}
  }

  @Test
  public void testRenamingFileToSameNameDoesNothing() {
    root.doCreateFile(p("file"), "content");

    root.doRename(p("file"), "file");

    assertTrue(root.hasEntry(p("file")));
    assertEquals("content", root.getEntry(p("file")).getContent());
  }

  @Test
  public void testDeletingFiles() {
    root.doCreateFile(p("file"), null);

    root.doDelete(p("file"));

    assertFalse(root.hasEntry(p("file")));
  }

  @Test
  public void testDeletingOnlyOneFile() {
    root.doCreateFile(p("file1"), null);
    root.doCreateFile(p("file2"), null);

    root.doDelete(p("file2"));

    assertTrue(root.hasEntry(p("file1")));
    assertFalse(root.hasEntry(p("file2")));
  }

  @Test
  public void testDeletingOfAnNonExistingFileThrowsException() {
    try {
      root.doDelete(p("file"));
      fail();
    } catch (LocalVcsException e) {}
  }
}
