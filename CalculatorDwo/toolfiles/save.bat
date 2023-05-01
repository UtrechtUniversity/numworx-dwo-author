%windir%\system32\xcopy.exe ..\src ..\versions\calculatordwo_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output ..\versions\calculatordwo_%1\output /E /I /Y
%windir%\system32\xcopy.exe ..\toolfiles ..\versions\calculatordwo_%1\toolfiles /E /I /Y
copy ..\readme.txt ..\versions\calculatordwo_%1
%windir%\system32\xcopy.exe ..\src M:\java\projecten\CalculatorDwo\versions\calculatordwo_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output M:\java\projecten\CalculatorDwo\versions\calculatordwo_%1\output /E /I /Y
%windir%\system32\xcopy.exe ..\toolfiles M:\java\projecten\CalculatorDwo\versions\calculatordwo_%1\toolfiles /E /I /Y
copy ..\readme.txt M:\java\projecten\CalculatorDwo\versions\calculatordwo_%1
