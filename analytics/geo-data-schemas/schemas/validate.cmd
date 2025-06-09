@ECHO off

setlocal EnableDelayedExpansion

set references=

echo Validating schema definitions...
echo.

for /r %%f in (definitions\*.json) do (
    call ajv compile -s %%f --extend-refs=fail
    if !errorlevel! neq 0 exit /b !errorlevel!
    set references=!references! -r %%f
)
set references=!references:~1!

echo.
echo Validating object schemas...

for /r %%f in (objects\*.json) do (
    call ajv compile -s %%f %references% --extend-refs=fail
    if !errorlevel! neq 0 exit /b !errorlevel!
)
echo.
echo Test valid objects...
echo.
echo Test all valid against generalFeature.json shema...

for /R ..\example_geojson\valid\ %%f in (*.json) do (
    call ajv test -s .\objects\generalFeature.json -d %%f -r %references% --valid --extend-refs=fail --errors=text
    if !errorlevel! neq 0 exit /b !errorlevel!
)
echo.

echo Test valid road sign example against roadSign.json schema...

call ajv test -s .\objects\roadSign.json -d ..\example_geojson\valid\general\roadSign.json -r %references% --valid --extend-refs=fail --errors=text
if !errorlevel! neq 0 exit /b !errorlevel!
echo.

echo Test valid pedestrian crossing example against roadSign.json schema...

call ajv test -s .\objects\pedestrianCrossing.json -d ..\example_geojson\valid\general\pedestrianCrossing.json -r %references% --valid --extend-refs=fail --errors=text
if !errorlevel! neq 0 exit /b !errorlevel!
echo.

echo Test valid roads example against road.json schema...

call ajv test -s .\objects\road.json -d ..\example_geojson\valid\general\roads.json -r %references% --valid --extend-refs=fail --errors=text
if !errorlevel! neq 0 exit /b !errorlevel!
echo.

echo Test valid speed limit zones against speedLimitZone.json schema...

for /r %%f in (..\example_geojson\valid\speed_limits\*.json) do (
    call ajv test -s .\objects\speedLimitZone.json -d %%f -r %references% --valid --extend-refs=fail --errors=text
    if !errorlevel! neq 0 exit /b !errorlevel!
)

echo.
echo Test valid no overtaking zones against noOvertakingZone.json schema...

for /r %%f in (..\example_geojson\valid\no_overtaking\*.json) do (
    call ajv test -s .\objects\noOvertakingZone.json -d %%f -r %references% --valid --extend-refs=fail --errors=text
    if !errorlevel! neq 0 exit /b !errorlevel!
)

echo.
echo Test invalid cases
echo.

echo Test invalid general properties...
for /r %%f in (..\example_geojson\invalid\general\*.json) do (
    call ajv test -s .\objects\generalFeature.json -d %%f -r %references% --invalid --extend-refs=fail --errors=text
    if !errorlevel! neq 0 exit /b !errorlevel!
)
echo.

echo Test invalid speed limit zone examples...
for /r %%f in (..\example_geojson\invalid\speed_limits\*.json) do (
    call ajv test -s .\objects\speedLimitZone.json -d %%f -r %references% --invalid --extend-refs=fail --errors=text
    if !errorlevel! neq 0 exit /b !errorlevel!
)
echo.

echo Test invalid no overtaking zone examples...
for /r %%f in (..\example_geojson\invalid\no_overtaking\*.json) do (
    call ajv test -s .\objects\noOvertakingZone.json -d %%f -r %references% --invalid --extend-refs=fail --errors=text
    if !errorlevel! neq 0 exit /b !errorlevel!
)
echo.
echo Passed all tests