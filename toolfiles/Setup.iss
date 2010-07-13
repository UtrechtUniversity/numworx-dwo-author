; -- Sample1.iss --
; Demonstrates copying 3 files and creating an icon.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=BinomVerdeling
AppVerName=BinomVerdeling version 1.0
AppCopyright=Copyright (C) Freudenthal Instituut.
DefaultDirName={pf}\Wisweb\BinomVerdeling
DefaultGroupName=Wisweb
UninstallDisplayIcon={app}\BinomVerdeling.exe
MessagesFile=compiler:Dutch-1-2_0_18.isl
OutputDir=..\output\setup

[Files]
Source: "..\output\exe\BinomVerdeling.exe"; DestDir: "{app}"


[Icons]
Name: "{group}\BinomVerdeling"; Filename: "{app}\BinomVerdeling.exe"