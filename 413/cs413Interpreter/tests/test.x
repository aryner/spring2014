LABEL X 
LIT 3 
FALSEBRANCH   Y 
LIT 7 
ARGS 1 
CALL WRITE 
GOTO Z 
LABEL Y 
ARGS 0 
CALL READ 
ARGS 1 
CALL WRITE 
LABEL Z 
HALT 
LABEL READ 
READ 
RETURN 
LABEL WRITE 
LOAD 0 
WRITE 
RETURN
