# ClassPageBuilder
 Builder that creates class documentation file.
 
 @author wangzhi

## Fields
### typeElement
Target type element this builder is working on. 
### environment
Doclet environment instance. 
## Methods
### getTypeFullName
 Gets the full name of the type, including outer class names for nested types.
 
 @return The full name of the type.

#### Returns
java.lang.String
### buildClassHeader
 Builds class header by writing class name and comment.
 
 @throws IOException If any error occurs while writing header.

#### Returns
void
#### Throws
* java.io.IOException
### buildFieldDocumentation
 Builds field documentation for the given field.
 
 @param field Field to build documentation for.
 @throws IOException If any error occurs while writing documentation.

#### Parameters
* field - javax.lang.model.element.VariableElement
#### Returns
void
#### Throws
* java.io.IOException
### buildFieldList
 Builds field list by processing each field of our target class.
 
 @throws IOException If any error occurs while writing field list.

#### Returns
void
#### Throws
* java.io.IOException
### buildMethodDocumentation
 Builds method documentation for the given method.
 
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
