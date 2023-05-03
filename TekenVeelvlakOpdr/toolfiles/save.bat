%windir%\system32\xcopy.exe ..\src ..\versions\tekenveelvlakopdr_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output ..\versions\tekenveelvlakopdr_%1\output /E /I /Y
%windir%\system32\xcopy.exe ..\toolfiles ..\versions\tekenveelvlakopdr_%1\toolfiles /E /I /Y
copy ..\readme.txt ..\versions\tekenveelvlakopdr_%1
%windir%\system32\xcopy.exe ..\src M:\java\projecten\TekenVeelvlakOpdr\versions\tekenveelvlakopdr_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output M:\java\projecten\TekenVeelvlakOpdr\versions\tekenveelvlakopdr_%1\output /E /I /Y
%windir%\system32\xcopy.exe ..\toolfiles M:\java\projecten\TekenVeelvlakOpdr\versions\tekenveelvlakopdr_%1\toolfiles /E /I /Y
copy ..\readme.txt M:\java\projecten\TekenVeelvlakOpdr\versions\tekenveelvlakopdr_%1
