%windir%\system32\xcopy.exe ..\src ..\versions\mozarch_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output ..\versions\mozarch_%1\output /E /I /Y
%windir%\system32\xcopy.exe ..\toolfiles ..\versions\mozarch_%1\toolfiles /E /I /Y
copy ..\readme.txt ..\versions\mozarch_%1
%windir%\system32\xcopy.exe ..\src M:\java\projecten\MozArch\versions\mozarch_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output M:\java\projecten\MozArch\versions\mozarch_%1\output /E /I /Y
%windir%\system32\xcopy.exe ..\toolfiles M:\java\projecten\MozArch\versions\mozarch_%1\toolfiles /E /I /Y
copy ..\readme.txt M:\java\projecten\MozArch\versions\mozarch_%1
