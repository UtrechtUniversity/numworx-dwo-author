%windir%\system32\xcopy.exe ..\src ..\versions\binomverdeling_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output ..\versions\binomverdeling_%1\output /E /I /Y
%windir%\system32\xcopy.exe ..\toolfiles ..\versions\binomverdeling_%1\toolfiles /E /I /Y
copy ..\readme.txt ..\versions\binomverdeling_%1
%windir%\system32\xcopy.exe ..\src M:\java\projecten\BinomVerdeling\versions\binomverdeling_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output M:\java\projecten\BinomVerdeling\versions\binomverdeling_%1\output /E /I /Y
%windir%\system32\xcopy.exe ..\toolfiles M:\java\projecten\BinomVerdeling\versions\binomverdeling_%1\toolfiles /E /I /Y
copy ..\readme.txt M:\java\projecten\BinomVerdeling\versions\binomverdeling_%1