%windir%\system32\xcopy.exe ..\src ..\versions\kansbomen_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output ..\versions\kansbomen_%1\output /E /I /Y
%windir%\system32\xcopy.exe ..\toolfiles ..\versions\kansbomen_%1\toolfiles /E /I /Y
copy ..\readme.txt ..\versions\kansbomen_%1
%windir%\system32\xcopy.exe ..\src M:\java\projecten\Kansbomen\versions\kansbomen_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output M:\java\projecten\Kansbomen\versions\kansbomen_%1\output /E /I /Y
%windir%\system32\xcopy.exe ..\toolfiles M:\java\projecten\Kansbomen\versions\kansbomen_%1\toolfiles /E /I /Y
copy ..\readme.txt M:\java\projecten\Kansbomen\versions\kansbomen_%1
