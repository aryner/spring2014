package lexer;


/**
 *  The Lexer class is responsible for scanning the source file
 *  which is a stream of characters and returning a stream of 
 *  tokens; each token object will contain the string (or access
 *  to the string) that describes the token along with an
 *  indication of its location in the source program to be used
 *  for error reporting; we are tracking line numbers; white spaces
 *  are space, tab, newlines
*/
public class Lexer {
    private boolean atEOF = false;
    private char ch;     // next character to process
    private SourceReader source;
    
    // positions in line of current token
    private int startPosition, endPosition, line; 

    public Lexer(String sourceFile) throws Exception {
        new TokenType();  // init token table
        source = new SourceReader(sourceFile);
        ch = source.read();
    }


    public static void main(String args[]) {
        Token tok;

        try {
            Lexer lex = new Lexer(args[0]);
            while (true) {
                tok = lex.nextToken();
/*
                String p = "L: " + tok.getLeftPosition() +
                   " L: " + tok.getRightPosition() + "  " +
                   TokenType.tokens.get(tok.getKind()) + " ";
                if ((tok.getKind() == Tokens.Identifier) ||
                    (tok.getKind() == Tokens.INTeger))
                    p += tok.toString();
                System.out.println(p + ": "+lex.source.getLineno());
*/
                if(!lex.atEOF) {
		    tok.print();
                }
                else {
                    break;
                }
            }
        } catch (Exception e) {}
    }

 
/**
 *  newIdTokens are either ids or reserved words; new id's will be inserted
 *  in the symbol table with an indication that they are id's
 *  @param id is the String just scanned - it's either an id or reserved word
 *  @param lineno is the line number where the token is
 *  @param startPosition is the column in the source file where the token begins
 *  @param endPosition is the column in the source file where the token ends
 *  @return the Token; either an id or one for the reserved words
*/
    public Token newIdToken(String id,int lineno, int startPosition,int endPosition) {
        return new Token(lineno,startPosition,endPosition,Symbol.symbol(id,Tokens.Identifier));
    }

/**
 *  number tokens are inserted in the symbol table; we don't convert the 
 *  numeric strings to numbers until we load the bytecodes for interpreting;
 *  this ensures that any machine numeric dependencies are deferred
 *  until we actually run the program; i.e. the numeric constraints of the
 *  hardware used to compile the source program are not used
 *  @param number is the int String just scanned
 *  @param lineno is the line number where the token is
 *  @param startPosition is the column in the source file where the int begins
 *  @param endPosition is the column in the source file where the int ends
 *  @return the int Token
*/
    public Token newNumberToken(String number,int lineno,int startPosition,int endPosition) {
        return new Token(lineno, startPosition,endPosition,
            Symbol.symbol(number,Tokens.INTeger));
    }

/**
 * @param number is the float String just scanned
 * @param lineno is the line number where the token is
 * @param startPosition is the column in the source file where the int begins
 * @param endPosition is the column in the source file where the int ends
 * @return the float Token
*/
    public Token newFloatToken(String number,int lineno,int startPosition,int endPosition) {
        return new Token(lineno, startPosition, endPosition,
            Symbol.symbol(number,Tokens.FloatingPoint));
    }

/**
 *  build the token for operators (+ -) or separators (parens, braces)
 *  filter out comments which begin with two slashes
 *  @param s is the String representing the token
 *  @param line is the line position of the token
 *  @param startPosition is the column in the source file where the token begins
 *  @param endPosition is the column in the source file where the token ends
 *  @return the Token just found
*/
    public Token makeToken(String s,int line,int startPosition,int endPosition) {
        if (s.equals("//")) {  // filter comment
            try {
               int oldLine = source.getLineno();
               do {
                   ch = source.read();
               } while (oldLine == source.getLineno());
            } catch (Exception e) {
                    atEOF = true;
            }
            return nextToken();
        }
        Symbol sym = Symbol.symbol(s,Tokens.BogusToken); // be sure it's a valid token
        if (sym == null) {
             System.out.println("******** illegal character: " + s);
             atEOF = true;
             return nextToken();
        }
        return new Token(line,startPosition,endPosition,sym);
    }

/**
 *  @return the next Token found in the source file
*/
    public Token nextToken() { // ch is always the next char to process
        if (atEOF) {
            if (source != null) {
                System.out.println();
                source.printLines();
                source.close();
                source = null;
            }
            return null;
        }
        try {
            while (Character.isWhitespace(ch)) {  // scan past whitespace
                ch = source.read();
            }
        } catch (Exception e) {
            atEOF = true;
            return nextToken();
        }
        startPosition = source.getPosition();
        endPosition = startPosition - 1;
        line = source.getLineno();

        if (Character.isJavaIdentifierStart(ch)) {
            // return tokens for ids and reserved words
            String id = "";
            try {
                do {
                    endPosition++;
                    id += ch;
                    ch = source.read();
                } while (Character.isJavaIdentifierPart(ch));
            } catch (Exception e) {
                atEOF = true;
            }
            return newIdToken(id,line,startPosition,endPosition);
        }
        if (Character.isDigit(ch)) {
            // return number tokens
            String number = "";
            try {
                do {
                    endPosition++;
                    number += ch;
                    ch = source.read();
                } while (Character.isDigit(ch));
                
                if(ch == '.') {
                    endPosition++;
                    number += ch;
                    ch = source.read();
                    while (Character.isDigit(ch)) {
                        endPosition++;
                        number += ch;
                        ch = source.read();
                    }
                    return newFloatToken(number,line,startPosition,endPosition);
                }
            } catch (Exception e) {
                atEOF = true;
            }
            return newNumberToken(number,line,startPosition,endPosition);
        }

        if(ch == '.') {
            String number = "";
            try{
                endPosition++;
                number += ch;
                ch = source.read();
                while (Character.isDigit(ch)) {
                    endPosition++;
                    number += ch;
                    ch = source.read();
                }
            } catch (Exception e) {
                atEOF = true;
            }
            if (endPosition == startPosition) {
                makeToken(number,line,startPosition,endPosition);
            }
            return newFloatToken(number,line,startPosition,endPosition);
        }
        
        // At this point the only tokens to check for are one or two
        // characters; we must also check for comments that begin with
        // 2 slashes
        String charOld = "" + ch;
        String op = charOld;
        Symbol sym;
        try {
            endPosition++;
            ch = source.read();
            op += ch;
            // check if valid 2 char operator; if it's not in the symbol
            // table then don't insert it since we really have a one char
            // token
            sym = Symbol.symbol(op, Tokens.BogusToken); 
            if (sym == null) {  // it must be a one char token
                return makeToken(charOld,line,startPosition,endPosition);
            }
            endPosition++;
            ch = source.read();
            return makeToken(op,line,startPosition,endPosition);
        } catch (Exception e) {}
        atEOF = true;
        if (startPosition == endPosition) {
            op = charOld;
        }
        return makeToken(op,line,startPosition,endPosition);
    }
}
