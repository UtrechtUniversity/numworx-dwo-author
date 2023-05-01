%windir%\system32\xcopy.exe ..\src ..\versions\spot_problems_dwo_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output ..\versions\spot_problems_dwo_%1\output /E /I /Y
%windir%\system32\xcopy.exe ..\toolfiles ..\versions\spot_problems_dwo_%1\toolfiles /E /I /Y
copy ..\readme.txt ..\versions\spot_problems_dwo_%1
%windir%\system32\xcopy.exe ..\src M:\java\projecten\Spot_Problems_dwo\versions\spot_problems_dwo_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output M:\java\projecten\Spot_Problems_dwo\versions\spot_problems_dwo_%1\output /E /I /Y
%windir%\system32\xcopy.exe ..\toolfiles M:\java\projecten\Spot_Problems_dwo\versions\spot_problems_dwo_%1\toolfiles /E /I /Y
copy ..\readme.txt M:\java\projecten\Spot_Problems_dwo\versions\spot_problems_dwo_%1
