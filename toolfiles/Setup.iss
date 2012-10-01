; -- Sample1.iss --
; Demonstrates copying 3 files and creating an icon.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=Kladje
AppVerName=Kladje version 1.0
AppCopyright=Copyright (C) Freudenthal Instituut.
DefaultDirName={pf}\Wisweb\Kladje
DefaultGroupName=Wisweb
UninstallDisplayIcon={app}\Kladje.exe
MessagesFile=compiler:Dutch-1-2_0_18.isl
OutputDir=..\output\setup

[Files]
Source: "..\output\exe\Kladje.exe"; DestDir: "{app}"


[Icons]
Name: "{group}\Kladje"; Filename: "{app}\Kladje.exe"
