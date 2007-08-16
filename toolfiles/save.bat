%windir%\system32\xcopy.exe ..\src ..\versions\javalogoweb_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output ..\versions\javalogoweb_%1\output /E /I /Y
%windir%\system32\xcopy.exe ..\toolfiles ..\versions\javalogoweb_%1\toolfiles /E /I /Y
copy ..\readme.txt ..\versions\javalogoweb_%1
%windir%\system32\xcopy.exe ..\src M:\java\projecten\JavaLogoWeb\versions\javalogoweb_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output M:\java\projecten\JavaLogoWeb\versions\javalogoweb_%1\output /E /I /Y
%windir%\system32\xcopy.exe ..\toolfiles M:\java\projecten\JavaLogoWeb\versions\javalogoweb_%1\toolfiles /E /I /Y
copy ..\readme.txt M:\java\projecten\JavaLogoWeb\versions\javalogoweb_%1
