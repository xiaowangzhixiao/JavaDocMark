# JavadocMarkDocumentBuilder
 Builder that creates markdown file for class documentation.
 
 @author wangzhi

## Fields
### typeElement
Target type element this builder is working on. 
### environment
Doclet environment instance. 
## Methods
### getPackageName
 Builds and returns the package name of the given type.
 
 @param type Type to get package name from.
 @return Package name of the given type.

#### Parameters
* type - javax.lang.model.type.TypeMirror
#### Returns
java.lang.String
### getClassName
 Builds and returns the class name of the given type.
 
 @param type Type to get class name from.
 @return Class name of the given type.

#### Parameters
* type - javax.lang.model.type.TypeMirror
#### Returns
java.lang.String
### processAnnotationValue
 Process the given annotation value and returns
 a valid markdown link if possible.
 
 @param value Annotation value to process.
 @return Optional link built from value if possible.

#### Parameters
* value - javax.lang.model.element.AnnotationValue
#### Returns
java.util.Optional<java.lang.String>
### processElement
 Process the given element and returns a valid
 markdown link if possible.
 
 @param element Element to process.
 @return Optional link built from element if possible.

#### Parameters
* element - javax.lang.model.element.Element
#### Returns
java.util.Optional<java.lang.String>
### buildMethodLink
 Builds a valid markdown link for the given method.
 
 @param method Method to build link for.
 @return Built link.

#### Parameters
* method - javax.lang.model.element.ExecutableElement
#### Returns
java.lang.String
### buildTypeLink
 Builds a valid markdown link for the given type.
 
 @param type Type to build link for.
 @return Built link.

#### Parameters
* type - javax.lang.model.type.TypeMirror
#### Returns
java.lang.String
### buildMethodDocumentation
 Builds documentation for the given method.
 
 @param method Method to build documentation for.
 @throws IOException If any error occurs while writing documentation.

#### Parameters
* method - javax.lang.model.element.ExecutableElement
#### Returns
void
#### Throws
* java.io.IOException
### buildParametersDocumentation
 Builds parameters documentation for the given method.
 
 @param method Method to build parameters documentation for.
 @throws IOException If any error occurs while writing documentation.

#### Parameters
* method - javax.lang.model.element.ExecutableElement
#### Returns
void
#### Throws
* java.io.IOException
### buildReturnDocumentation
 Builds return type documentation for the given method.
 
 @param method Method to build return type documentation for.
 @throws IOException If any error occurs while writing documentation.

#### Parameters
* method - javax.lang.model.element.ExecutableElement
#### Returns
void
#### Throws
* java.io.IOException
### buildThrowsDocumentation
 Builds throws documentation for the given method.
 
 @param method Method to build throws documentation for.
 @throws IOException If any error occurs while writing documentation.

#### Parameters
* method - javax.lang.model.element.ExecutableElement
#### Returns
void
#### Throws
* java.io.IOException
### buildClassHeader
 Builds class header by writing class name and comment.
 
 @throws IOException If any error occurs while writing header.

#### Returns
void
#### Throws
* java.io.IOException
### buildMethodList
 Builds method list by processing each method of our target class.
 
 @throws IOException If any error occurs while writing method list.

#### Returns
void
#### Throws
* java.io.IOException
### build
{@inheritDoc} 
#### Parameters
* output - java.nio.file.Path
#### Returns
void
#### Throws
* java.io.IOException
