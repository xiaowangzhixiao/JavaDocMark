# MarkdownDocumentBuilder
 This class aims to build Markdown document.
 It is built in a top of a {@link StringBuffer}
 instance which will contains our document
 content.
 
 @author wangzhi

## Fields
### LINK_EXTENSION
Extension used for linked file. 
### FILE_EXTENSION
Extension used for generated markdown file. 
### BOLD
Bold text decoration. 
### CODE
Inline code snippets 
### ITALIC
Italic text decoration. 
### LIST_ITEM
List item prefix. 
### QUOTE
Quoted text prefix. 
### BR
Breaking return sequence. 
### HR
Horizontal rule sequence. 
### ROW_START
Table row start prefix. 
### CELL_SEPARATOR
Table row cell separator. 
### ROW_END
Table row end suffix. 
### LINK_OPEN
HTML link opening tag. 
### LINK_CLOSE
HTML link closing tag. 
### PARAGRAPH_OPEN
HTML paragraph opening tag. 
### PARAGRAPH_CLOSE
HTML paragraph closing tag. 
### buffer
Buffer in which markdown document is stored. 
## Methods
### filterParagraph
 Filters and returns the given ``text``
 freed from HTML paragraph.
 
 @param text Text to remove paragraph tag from.
 @return Filtered text.

#### Parameters
* text - java.lang.String
#### Returns
java.lang.String
### newLine
 Appends a new line to the current document.

#### Returns
void
### text
 Appends the given ``text`` to the current
 document.
 
 @param text Text to append to the document.

#### Parameters
* text - java.lang.String
#### Returns
void
### character
 Appends the given ``character`` to the current
 document.
 
 @param character Character to append to the document.

#### Parameters
* character - char
#### Returns
void
### bold
 Appends the given ``text`` to the current
 document with a bold decoration.
 
 @param text Text to append to the document with the bold decoration.

#### Parameters
* text - java.lang.String
#### Returns
void
### code
Apppends this given `text` to the current document with a
 code decoration.
 @param text code snippet to add to the document

#### Parameters
* text - java.lang.String
#### Returns
void
### italic
 Appends the given ``text`` to the current
 document with an italic decoration.
 
 @param text Text to append to the document with the italic decoration.

#### Parameters
* text - java.lang.String
#### Returns
void
### quote
 Starts a quote in the current document.

#### Returns
void
### item
 Starts a list item in the current document.

#### Returns
void
### horizontalRule
 Appends a horizontal rule sequence 
 to the current document.

#### Returns
void
### breakingReturn
 Appends a horizontal rule sequence
 to the current document.

#### Returns
void
### header
 Starts a header text, in the current document
 using the given header ``level``
 
 @param level Level of the header to start.

#### Parameters
* level - int
#### Returns
void
### header
 Starts a header text with the given text, in the current document
 using the given header ``level``
 
 @param level Level of the header to start.
 @param text Text to append after the header.

#### Parameters
* level - int
* text - java.lang.String
#### Returns
void
### rawLink
 Appends a raw HTML link to the current document
 using the given ``label`` and the given
 ``url``.
 
 @param label Label of the built link.
 @param url Target URL of the built link.

#### Parameters
* label - java.lang.String
* url - java.lang.String
#### Returns
void
### link
 Appends a markdown link to the current document
 using the given ``label`` and the given
 ``url``.
 
 @param label Label of the built link.
 @param url Target URL of the built link.

#### Parameters
* label - java.lang.String
* url - java.lang.String
#### Returns
void
### startTableRow
 Starts a table row.

#### Returns
void
### cell
 Adds a cell separator.

#### Returns
void
### endTableRow
 Ends a table row.

#### Returns
void
### tableHeader
 Creates a table header row with the given headers.
 
 @param headers Headers to use.

#### Parameters
* headers - java.lang.String[]
#### Returns
void
### tableRow
 Creates a table row with the given cells.
 
 @param cells Cells to use.

#### Parameters
* cells - java.lang.String[]
#### Returns
void
### build
 Builds and returns the current document content.
 
 @return Built document content.

#### Returns
java.lang.String
### build
 Builds and writes the document to the given path.
 
 @param path Path to write the document to.
 @throws IOException If any error occurs while writing the document.

#### Parameters
* path - java.nio.file.Path
#### Returns
void
#### Throws
* java.io.IOException
