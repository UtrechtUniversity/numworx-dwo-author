; -- Sample1.iss --
; Demonstrates copying 3 files and creating an icon.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=JavaLogoWeb
AppVerName=JavaLogoWeb version 1.0
AppCopyright=Copyright (C) Freudenthal Instituut.
DefaultDirName={pf}\Wisweb\JavaLogoWeb
DefaultGroupName=Wisweb
UninstallDisplayIcon={app}\JavaLogoWeb.exe
MessagesFile=compiler:Dutch-1-2_0_18.isl
OutputDir=..\output\setup

[Files]
Source: "..\output\exe\JavaLogoWeb.exe"; DestDir: "{app}"


[Icons]
Name: "{group}\JavaLogoWeb"; Filename: "{app}\JavaLogoWeb.exe"
