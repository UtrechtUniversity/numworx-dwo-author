%windir%\system32\xcopy.exe ..\src ..\versions\statistiek_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output ..\versions\statistiek_%1\output /E /I /Y
copy ..\readme.txt ..\versions\statistiek_%1
