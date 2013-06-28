%windir%\system32\xcopy.exe ..\src ..\versions\draaibank_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output ..\versions\draaibank_%1\output /E /I /Y
copy ..\readme.txt ..\versions\draaibank_%1
