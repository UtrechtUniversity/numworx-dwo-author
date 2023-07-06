%windir%\system32\xcopy.exe ..\src ..\versions\balansfruitapplet_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output ..\versions\balansfruitapplet_%1\output /E /I /Y
%windir%\system32\xcopy.exe ..\toolfiles ..\versions\balansfruitapplet_%1\toolfiles /E /I /Y
copy ..\readme.txt ..\versions\balansfruitapplet_%1
%windir%\system32\xcopy.exe ..\src M:\java\projecten\BalansFruitApplet\versions\balansfruitapplet_%1\src /E /I /Y
%windir%\system32\xcopy.exe ..\output M:\java\projecten\BalansFruitApplet\versions\balansfruitapplet_%1\output /E /I /Y
%windir%\system32\xcopy.exe ..\toolfiles M:\java\projecten\BalansFruitApplet\versions\balansfruitapplet_%1\toolfiles /E /I /Y
copy ..\readme.txt M:\java\projecten\BalansFruitApplet\versions\balansfruitapplet_%1