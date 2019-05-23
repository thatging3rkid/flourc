# The Flour Programming Language Specification

## Chapter 1. Introduction

Flour is a C-derived, general-purpose, object-oriented programming language. It's primary implementation (`flourc`) has been designed to compile code to LLVM IR, however the language could theoretically be implemented as an interpreter. This specification is based on the Java SE 8 spec, which can be found [here](https://docs.oracle.com/javase/specs/jls/se8/jls8.pdf). The language is another attempt at 'C with classes', so it is influenced by from C, C++, Java, Python, C#, Go and Rust.

## Chapter 2. Structure

### Section 2.1 File Structure

Flour programs are saved in a Unicode format, using the `\n` newline character. `\n\r` is converted to a `\n` automatically.

Flour programs (denoted with the .flour extension) consist of pre-processor commands, imports, and code, where code can be a group of valid Flour statements, functions, classes, etc. These different sections can be interlaced, but generally, imports are at the top of the file (because imports take effect in the lines following the import).

## Section 2.2 Statement Structure

In Flour, code statements are broken into lines, where a line is a group of tokens that ends in a semi-colon (';'). However, when an in-line comment is detected, the end of a line is considered to be the `\n` character, so anything between the `//` and `\n` is ignored by the compiler before preprocessing. The same logic can be applied to block comments, where any characters between the `/*` and `*/` is ignored by the compiler before preprocessing. Block comments take precedence over in-line comments. During the lexing process, which takes place before the preprocessor, whitespace is used to tokenize the file contents, then is discarded.

### Subsection 2.2.1 Keywords

The following are keywords in Flour:

`for do while if else continue break switch case lambda new ptr import class public private get set const enum parent test` (this list will be developed as the language progresses)

### Subsection 2.2.2 Operators

The following tokens are operators in Flour:
```
=  >  <  !  ~  -> 
== >= <= != && || ++ --
+  -  *  /  &  |  ^  %  << >>
+= -= *= /= &= |= ^= %= 

``` (again, this list will be developed as the language progresses)

The following tokens act as seperators (the tokenizer splits code using them, but they are not used in assignment operations):

`( ) { } [ ] ; , .`

All preprocessor commands start with the `#` character.

### Subsection 2.2.3 Literals

The null literal is `null` and represents an object with a memory address of 0x0000.

Integer literals can be given in decimal, hex, octal or binary. When given in hex, octal or binary, the literals must be preceeded by a `0x` for hex, `0o` for octal or `0b` for binary (upper case is accepted, but highly discouraged, especially for octal). An integer literal must start with a decimal digit (defined as a `0 1 2 3 4 5 6 7 8 9` from here forward). If the length of the token is greater than 2 and the first two characters match a preceeding character, then the rest of the token is parsed in the given base, otherwise the token is parsed as a decimal.

Boolean literals either are `true` or `false`.

Character literals consist a group of single-quotes enclosing a single Unicode character.

String literals consist of a group of double-quotes enclosing an unlimited number of Unicode characters. String literals can be preceeded by an `m` to cause the string to be interpreted as a multi-line string (any whitespace characters are preserved as the are in the code) or an `r` to cause the string to be interpreted as a raw-string (no escape characters are accepted, with the execption of `\"`).

The following are valid escape characters in Flour:

```
\t \n \r \" \' \\ \u
```

Any `\` that does not have a matching entry in the above table will cause an error.

## Chapter 3. Types

## Chapter 4. The Preprocessor

## Chapter 5. Classes

## Chapter 6. Error Handling
