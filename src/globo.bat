@Echo off
cls
del %1.sal 2> null
java globoAL %1
if errorlevel 1 goto Fallo
java globoSL1 %1
:Fallo