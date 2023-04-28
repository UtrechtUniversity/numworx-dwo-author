; -- Sample1.iss --
; Demonstrates copying 3 files and creating an icon.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=Heks
AppVerName=Heks version 1.0
AppCopyright=Copyright (C) Freudenthal Instituut.
DefaultDirName={pf}\Wisweb\Heks
DefaultGroupName=Wisweb
UninstallDisplayIcon={app}\Heks.exe
MessagesFile=compiler:Dutch-1-2_0_18.isl
OutputDir=..\output\setup

[Files]
Source: "..\output\exe\Heks.exe"; DestDir: "{app}"


[Icons]
Name: "{group}\Heks"; Filename: "{app}\Heks.exe"
