
$Languages = @{
    # General-Purpose Languages
    "S"      = "Assembly";
    "c"      = "C";
    "h"      = "C Headers";
    "cc"     = "C++";
    "hh"     = "C++ Headers";
    "java"   = "Java";
    "rs"     = "Rust";
    "sql"    = "SQL";
    # GPU Languages
    "cl"     = "OpenCL";
    "glsl"   = "GLSL";
    "hlsl"   = "HLSL";
    # Scripting Languages
    "lua"    = "Lua";
    "ps1"    = "PowerShell";
    "py"     = "Python"
}

$DataFormat = @(
    @{
        Name       = "Language";
        Expression = { $Languages[$_.Name.Substring(1)] }
    },
    @{
        Name       = "FileExtension";
        Expression = { $_.Name }
    },
    @{
        Name       = "LinesOfCode";
        Expression = { ($_.Group | Get-Content | Measure-Object -Line).Lines }
    },
    @{
        Name       = "FileCount";
        Expression = { $_.Group.Count }
    }
)

$Filter = "^\.({0})$" -F ($Languages.Keys -Join "|")

$Stats =
    Get-ChildItem -Recurse |
    Where-Object Extension -Match $Filter |
    Group-Object Extension |
    Select-Object $DataFormat |
    Sort-Object LinesOfCode

$Stats | Format-Table @(
    @{
        Name       = "Language";
        Expression = { $_.Language }
    },
    @{
        Name       = "File Extension";
        Expression = { $_.FileExtension }
    },
    @{
        Name       = "Lines of Code";
        Expression = { "{0:N0}" -F $_.LinesOfCode };
        Alignment  = "Right"
    },
    @{
        Name       = "File Count";
        Expression = { "{0:N0}" -F $_.FileCount };
        Alignment  = "Right"
    }
)
"Total Lines of Code: {0,9:N0}" -F ($Stats.LinesOfCode | Measure-Object -Sum).Sum
"Total File Count:    {0,9:N0}" -F ($Stats.FileCount | Measure-Object -Sum).Sum
""