; -- Sample1.iss --
; Demonstrates copying 3 files and creating an icon.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=Verknippen
AppVerName=Verknippen version 1.0
AppCopyright=Copyright (C) Freudenthal Instituut.
DefaultDirName={pf}\Wisweb\Verknippen
DefaultGroupName=Wisweb
UninstallDisplayIcon={app}\Verknippen.exe
MessagesFile=compiler:Dutch-1-2_0_18.isl
OutputDir=..\output\setup

[Files]
Source: "..\output\exe\Verknippen.exe"; DestDir: "{app}"


[Icons]
Name: "{group}\Verknippen"; Filename: "{app}\Verknippen.exe"