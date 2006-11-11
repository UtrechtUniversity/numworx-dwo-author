; -- Sample1.iss --
; Demonstrates copying 3 files and creating an icon.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=AlgebraPijlenOpdr
AppVerName=AlgebraPijlenOpdr version 1.0
AppCopyright=Copyright (C) Freudenthal Instituut.
DefaultDirName={pf}\Wisweb\AlgebraPijlenOpdr
DefaultGroupName=Wisweb
UninstallDisplayIcon={app}\AlgebraPijlenOpdr.exe
MessagesFile=compiler:Dutch-1-2_0_18.isl
OutputDir=..\output\setup

[Files]
Source: "..\output\exe\AlgebraPijlenOpdr.exe"; DestDir: "{app}"


[Icons]
Name: "{group}\AlgebraPijlenOpdr"; Filename: "{app}\AlgebraPijlenOpdr.exe"
