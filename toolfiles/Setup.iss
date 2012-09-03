; -- Sample1.iss --
; Demonstrates copying 3 files and creating an icon.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=Kansbomen
AppVerName=Kansbomen version 1.0
AppCopyright=Copyright (C) Freudenthal Instituut.
DefaultDirName={pf}\Wisweb\Kansbomen
DefaultGroupName=Wisweb
UninstallDisplayIcon={app}\Kansbomen.exe
MessagesFile=compiler:Dutch-1-2_0_18.isl
OutputDir=..\output\setup

[Files]
Source: "..\output\exe\Kansbomen.exe"; DestDir: "{app}"


[Icons]
Name: "{group}\Kansbomen"; Filename: "{app}\Kansbomen.exe"
