; -- Sample1.iss --
; Demonstrates copying 3 files and creating an icon.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=BalansFruitApplet
AppVerName=BalansFruitApplet version 1.0
AppCopyright=Copyright (C) Freudenthal Instituut.
DefaultDirName={pf}\Wisweb\BalansFruitApplet
DefaultGroupName=Wisweb
UninstallDisplayIcon={app}\BalansFruitApplet.exe
MessagesFile=compiler:Dutch-1-2_0_18.isl
OutputDir=..\output\setup

[Files]
Source: "..\output\exe\BalansFruitApplet.exe"; DestDir: "{app}"


[Icons]
Name: "{group}\BalansFruitApplet"; Filename: "{app}\BalansFruitApplet.exe"