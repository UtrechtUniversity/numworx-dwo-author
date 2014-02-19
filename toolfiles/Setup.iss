; -- Sample1.iss --
; Demonstrates copying 3 files and creating an icon.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=Statistiek
AppVerName=Statistiek version 1.0
AppCopyright=Copyright (C) Freudenthal Instituut.
DefaultDirName={pf}\Wisweb\Statistiek
DefaultGroupName=Wisweb
UninstallDisplayIcon={app}\Statistiek.exe
MessagesFile=compiler:Dutch-1-2_0_18.isl
OutputDir=..\output\setup

[Files]
Source: "..\output\exe\Statistiek.exe"; DestDir: "{app}"


[Icons]
Name: "{group}\Statistiek"; Filename: "{app}\Statistiek.exe"
