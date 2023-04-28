%windir%\system32\xcopy.exe ..\src ..\versions\heks_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output ..\versions\heks_%1\output /E /I /Y
%windir%\system32\xcopy.exe ..\toolfiles ..\versions\heks_%1\toolfiles /E /I /Y
copy ..\readme.txt ..\versions\heks_%1
%windir%\system32\xcopy.exe ..\src M:\java\projecten\Heks\versions\heks_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output M:\java\projecten\Heks\versions\heks_%1\output /E /I /Y
%windir%\system32\xcopy.exe ..\toolfiles M:\java\projecten\Heks\versions\heks_%1\toolfiles /E /I /Y
copy ..\readme.txt M:\java\projecten\Heks\versions\heks_%1
