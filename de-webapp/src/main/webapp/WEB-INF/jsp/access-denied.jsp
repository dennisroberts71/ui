<!DOCTYPE html>
<html>
<head>

<style>
    
    @font-face {
        font-family: TextaRegular;
        src: url(../../Texta_Font/Texta-Regular.otf);
    }

    @font-face {
        font-family: TextaBlack;
        src: url(../../Texta_Font/Texta-Black.otf);
    }


    body {
    font-family: TextaRegular, Univers,Calibri,"Gill Sans","Gill Sans MT","Myriad Pro",Myriad,"DejaVu Sans Condensed","Liberation Sans","Nimbus Sans L",Tahoma,Geneva,"Helvetica Neue",Helvetica,Arial,sans-serif;
    font-size: 0.9em;
    background-color: #e2e2e2;
    background-image: linear-gradient( 180deg, #FFF,#e2e2e2);
    background-repeat: repeat-x;
}
h1 {
    color: #0971AB;
    font-size: 1.5em;
    line-height: .5em;
    font-family: TextaBlack, Univers, Calibri, "Gill Sans", "Gill Sans MT", "Myriad Pro",
    Myriad, "DejaVu Sans Condensed", "Liberation Sans", "Nimbus Sans L",
    Tahoma, Geneva, "Helvetica Neue", Helvetica, Arial,
    sans-serif !important;
}
a {
    color: #0971AB;
    text-decoration: none;
}
a:hover {
    text-decoration: none;
    border-bottom: 1px dotted #0971AB;
}
.container {
    position: relative;
    top: 100px;
    min-height: 200px;
    margin-left: auto;
    margin-right: auto;
    padding: 10px;
    max-width: 500px;
    border-radius: 5px;
    border: 1px solid #BBB;
    background-color: #FFF;
    background-image: linear-gradient(bottom, #E2E2E2 0%, #FFF 100%);
    background-image: -o-linear-gradient(bottom, #E2E2E2 0%, #FFF 100%);
    background-image: -moz-linear-gradient(bottom, #E2E2E2 0%, #FFF 100%);
    background-image: -webkit-linear-gradient(bottom, #E2E2E2 0%, #FFF 100%);
    background-image: -ms-linear-gradient(bottom, #E2E2E2 0%, #FFF 100%);
    background-image: -webkit-gradient(
        linear,
        left bottom,
        left top,
        color-stop(0, #E2E2E2),
        color-stop(1, #FFF)
    );;
}
.darkBlueButton {
    color: #FFF;
    background-color: #185DA2;
    cursor: pointer;
    padding: 10px 30px;
    border-radius: 5px;
    border: 1px solid #424142;
    background-image: linear-gradient(bottom, #185DA2 0%, #2989E6 100%);
    background-image: -o-linear-gradient(bottom, #185DA2 0%, #2989E6 100%);
    background-image: -moz-linear-gradient(bottom, #185DA2 0%, #2989E6 100%);
    background-image: -webkit-linear-gradient(bottom, #185DA2 0%, #2989E6 100%);
    background-image: -ms-linear-gradient(bottom, #185DA2 0%, #2989E6 100%);
    font-family: Verdana;
    font-size: 11px;
    background-image: -webkit-gradient(
        linear,
        left bottom,
        left top,
        color-stop(0, #185DA2),
        color-stop(1, #2989E6)
    );
    text-decoration: none;
}
.darkBlueButton:hover {
    background-image: linear-gradient(bottom, #2989E6 0%, #185DA2 100%);
    background-image: -o-linear-gradient(bottom, #2989E6 0%, #185DA2 100%);
    background-image: -moz-linear-gradient(bottom, #2989E6 0%, #185DA2 100%);
    background-image: -webkit-linear-gradient(bottom, #2989E6 0%, #185DA2 100%);
    background-image: -ms-linear-gradient(bottom, #2989E6 0%, #185DA2 100%);
    background-image: -webkit-gradient(
        linear,
        left bottom,
        left top,
        color-stop(0, #2989E6),
        color-stop(1, #185DA2)
    );
}
.darkBlueButton:active {
    background-image: linear-gradient(bottom, #2989E6 0%, #246EBE 87%, #185DA2 100%);
    background-image: -o-linear-gradient(bottom, #2989E6 0%, #246EBE 87%, #185DA2 100%);
    background-image: -moz-linear-gradient(bottom, #2989E6 0%, #246EBE 87%, #185DA2 100%);
    background-image: -webkit-linear-gradient(bottom, #2989E6 0%, #246EBE 87%, #185DA2 100%);
    background-image: -ms-linear-gradient(bottom, #2989E6 0%, #246EBE 87%, #185DA2 100%);
    background-image: -webkit-gradient(
            linear,
            left bottom,
            left top,
            color-stop(0, #2989E6),
            color-stop(0.87, #246EBE),
            color-stop(1, #185DA2)
    );
}
.defaultButton {
    color: #414042;
    background-color: #BCBEC0;
    cursor: pointer;
    padding: 8px 23px;
    border-radius: 5px;
    border: 1px solid #414042;
    background-image: linear-gradient(bottom, #BCBEC0 0%, #F1F2F2 100%);
    background-image: -o-linear-gradient(bottom, #BCBEC0 0%, #F1F2F2 100%);
    background-image: -moz-linear-gradient(bottom, #BCBEC0 0%, #F1F2F2 100%);
    background-image: -webkit-linear-gradient(bottom, #BCBEC0 0%, #F1F2F2 100%);
    background-image: -ms-linear-gradient(bottom, #BCBEC0 0%, #F1F2F2 100%);
    font-family: Verdana;
    font-size: 11px;
    background-image: -webkit-gradient(
        linear,
        left bottom,
        left top,
        color-stop(0, #BCBEC0),
        color-stop(1, #F1F2F2)
    );
    text-decoration: none;
}
.defaultButton:hover {
    background-image: linear-gradient(bottom, #F1F2F2 0%, #BCBEC0 100%);
    background-image: -o-linear-gradient(bottom, #F1F2F2 0%, #BCBEC0 100%);
    background-image: -moz-linear-gradient(bottom, #F1F2F2 0%, #BCBEC0 100%);
    background-image: -webkit-linear-gradient(bottom, #F1F2F2 0%, #BCBEC0 100%);
    background-image: -ms-linear-gradient(bottom, #F1F2F2 0%, #BCBEC0 100%);
    background-image: -webkit-gradient(
        linear,
        left bottom,
        left top,
        color-stop(0, #F1F2F2),
        color-stop(1, #BCBEC0)
    );
}
.defaultButton:active {
    background-image: linear-gradient(bottom, #F1F2F2 0%, #BCBEC0 87%, #A7A9AC 100%);
    background-image: -o-linear-gradient(bottom, #F1F2F2 0%, #BCBEC0 87%, #A7A9AC 100%);
    background-image: -moz-linear-gradient(bottom, #F1F2F2 0%, #BCBEC0 87%, #A7A9AC 100%);
    background-image: -webkit-linear-gradient(bottom, #F1F2F2 0%, #BCBEC0 87%, #A7A9AC 100%);
    background-image: -ms-linear-gradient(bottom, #F1F2F2 0%, #BCBEC0 87%, #A7A9AC 100%);
    background-image: -webkit-gradient(
        linear,
        left bottom,
        left top,
        color-stop(0, #F1F2F2),
        color-stop(0.87, #BCBEC0),
        color-stop(1, #A7A9AC)
    );
}
</style>

</head>
<body>

<img src="../cyverse_logo.png" height="107px" width="500px"/>

    <div class="container">

    <img style="float: left; margin-right: 10px; position: relative; top: 8px"
         src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAACXBIWXMAAAsTAAAL
EwEAmpwYAAAKT2lDQ1BQaG90b3Nob3AgSUNDIHByb2ZpbGUAAHjanVNnVFPpFj33
3vRCS4iAlEtvUhUIIFJCi4AUkSYqIQkQSoghodkVUcERRUUEG8igiAOOjoCMFVEs
DIoK2AfkIaKOg6OIisr74Xuja9a89+bN/rXXPues852zzwfACAyWSDNRNYAMqUIe
EeCDx8TG4eQuQIEKJHAAEAizZCFz/SMBAPh+PDwrIsAHvgABeNMLCADATZvAMByH
/w/qQplcAYCEAcB0kThLCIAUAEB6jkKmAEBGAYCdmCZTAKAEAGDLY2LjAFAtAGAn
f+bTAICd+Jl7AQBblCEVAaCRACATZYhEAGg7AKzPVopFAFgwABRmS8Q5ANgtADBJ
V2ZIALC3AMDOEAuyAAgMADBRiIUpAAR7AGDIIyN4AISZABRG8lc88SuuEOcqAAB4
mbI8uSQ5RYFbCC1xB1dXLh4ozkkXKxQ2YQJhmkAuwnmZGTKBNA/g88wAAKCRFRHg
g/P9eM4Ors7ONo62Dl8t6r8G/yJiYuP+5c+rcEAAAOF0ftH+LC+zGoA7BoBt/qIl
7gRoXgugdfeLZrIPQLUAoOnaV/Nw+H48PEWhkLnZ2eXk5NhKxEJbYcpXff5nwl/A
V/1s+X48/Pf14L7iJIEyXYFHBPjgwsz0TKUcz5IJhGLc5o9H/LcL//wd0yLESWK5
WCoU41EScY5EmozzMqUiiUKSKcUl0v9k4t8s+wM+3zUAsGo+AXuRLahdYwP2SycQ
WHTA4vcAAPK7b8HUKAgDgGiD4c93/+8//UegJQCAZkmScQAAXkQkLlTKsz/HCAAA
RKCBKrBBG/TBGCzABhzBBdzBC/xgNoRCJMTCQhBCCmSAHHJgKayCQiiGzbAdKmAv
1EAdNMBRaIaTcA4uwlW4Dj1wD/phCJ7BKLyBCQRByAgTYSHaiAFiilgjjggXmYX4
IcFIBBKLJCDJiBRRIkuRNUgxUopUIFVIHfI9cgI5h1xGupE7yAAygvyGvEcxlIGy
UT3UDLVDuag3GoRGogvQZHQxmo8WoJvQcrQaPYw2oefQq2gP2o8+Q8cwwOgYBzPE
bDAuxsNCsTgsCZNjy7EirAyrxhqwVqwDu4n1Y8+xdwQSgUXACTYEd0IgYR5BSFhM
WE7YSKggHCQ0EdoJNwkDhFHCJyKTqEu0JroR+cQYYjIxh1hILCPWEo8TLxB7iEPE
NyQSiUMyJ7mQAkmxpFTSEtJG0m5SI+ksqZs0SBojk8naZGuyBzmULCAryIXkneTD
5DPkG+Qh8lsKnWJAcaT4U+IoUspqShnlEOU05QZlmDJBVaOaUt2ooVQRNY9aQq2h
tlKvUYeoEzR1mjnNgxZJS6WtopXTGmgXaPdpr+h0uhHdlR5Ol9BX0svpR+iX6AP0
dwwNhhWDx4hnKBmbGAcYZxl3GK+YTKYZ04sZx1QwNzHrmOeZD5lvVVgqtip8FZHK
CpVKlSaVGyovVKmqpqreqgtV81XLVI+pXlN9rkZVM1PjqQnUlqtVqp1Q61MbU2ep
O6iHqmeob1Q/pH5Z/YkGWcNMw09DpFGgsV/jvMYgC2MZs3gsIWsNq4Z1gTXEJrHN
2Xx2KruY/R27iz2qqaE5QzNKM1ezUvOUZj8H45hx+Jx0TgnnKKeX836K3hTvKeIp
G6Y0TLkxZVxrqpaXllirSKtRq0frvTau7aedpr1Fu1n7gQ5Bx0onXCdHZ4/OBZ3n
U9lT3acKpxZNPTr1ri6qa6UbobtEd79up+6Ynr5egJ5Mb6feeb3n+hx9L/1U/W36
p/VHDFgGswwkBtsMzhg8xTVxbzwdL8fb8VFDXcNAQ6VhlWGX4YSRudE8o9VGjUYP
jGnGXOMk423GbcajJgYmISZLTepN7ppSTbmmKaY7TDtMx83MzaLN1pk1mz0x1zLn
m+eb15vft2BaeFostqi2uGVJsuRaplnutrxuhVo5WaVYVVpds0atna0l1rutu6cR
p7lOk06rntZnw7Dxtsm2qbcZsOXYBtuutm22fWFnYhdnt8Wuw+6TvZN9un2N/T0H
DYfZDqsdWh1+c7RyFDpWOt6azpzuP33F9JbpL2dYzxDP2DPjthPLKcRpnVOb00dn
F2e5c4PziIuJS4LLLpc+Lpsbxt3IveRKdPVxXeF60vWdm7Obwu2o26/uNu5p7ofc
n8w0nymeWTNz0MPIQ+BR5dE/C5+VMGvfrH5PQ0+BZ7XnIy9jL5FXrdewt6V3qvdh
7xc+9j5yn+M+4zw33jLeWV/MN8C3yLfLT8Nvnl+F30N/I/9k/3r/0QCngCUBZwOJ
gUGBWwL7+Hp8Ib+OPzrbZfay2e1BjKC5QRVBj4KtguXBrSFoyOyQrSH355jOkc5p
DoVQfujW0Adh5mGLw34MJ4WHhVeGP45wiFga0TGXNXfR3ENz30T6RJZE3ptnMU85
ry1KNSo+qi5qPNo3ujS6P8YuZlnM1VidWElsSxw5LiquNm5svt/87fOH4p3iC+N7
F5gvyF1weaHOwvSFpxapLhIsOpZATIhOOJTwQRAqqBaMJfITdyWOCnnCHcJnIi/R
NtGI2ENcKh5O8kgqTXqS7JG8NXkkxTOlLOW5hCepkLxMDUzdmzqeFpp2IG0yPTq9
MYOSkZBxQqohTZO2Z+pn5mZ2y6xlhbL+xW6Lty8elQfJa7OQrAVZLQq2QqboVFoo
1yoHsmdlV2a/zYnKOZarnivN7cyzytuQN5zvn//tEsIS4ZK2pYZLVy0dWOa9rGo5
sjxxedsK4xUFK4ZWBqw8uIq2Km3VT6vtV5eufr0mek1rgV7ByoLBtQFr6wtVCuWF
fevc1+1dT1gvWd+1YfqGnRs+FYmKrhTbF5cVf9go3HjlG4dvyr+Z3JS0qavEuWTP
ZtJm6ebeLZ5bDpaql+aXDm4N2dq0Dd9WtO319kXbL5fNKNu7g7ZDuaO/PLi8ZafJ
zs07P1SkVPRU+lQ27tLdtWHX+G7R7ht7vPY07NXbW7z3/T7JvttVAVVN1WbVZftJ
+7P3P66Jqun4lvttXa1ObXHtxwPSA/0HIw6217nU1R3SPVRSj9Yr60cOxx++/p3v
dy0NNg1VjZzG4iNwRHnk6fcJ3/ceDTradox7rOEH0x92HWcdL2pCmvKaRptTmvtb
Ylu6T8w+0dbq3nr8R9sfD5w0PFl5SvNUyWna6YLTk2fyz4ydlZ19fi753GDborZ7
52PO32oPb++6EHTh0kX/i+c7vDvOXPK4dPKy2+UTV7hXmq86X23qdOo8/pPTT8e7
nLuarrlca7nuer21e2b36RueN87d9L158Rb/1tWeOT3dvfN6b/fF9/XfFt1+cif9
zsu72Xcn7q28T7xf9EDtQdlD3YfVP1v+3Njv3H9qwHeg89HcR/cGhYPP/pH1jw9D
BY+Zj8uGDYbrnjg+OTniP3L96fynQ89kzyaeF/6i/suuFxYvfvjV69fO0ZjRoZfy
l5O/bXyl/erA6xmv28bCxh6+yXgzMV70VvvtwXfcdx3vo98PT+R8IH8o/2j5sfVT
0Kf7kxmTk/8EA5jz/GMzLdsAAAAgY0hSTQAAeiUAAICDAAD5/wAAgOkAAHUwAADq
YAAAOpgAABdvkl/FRgAAAmFJREFUeNrsmstR6zAUhn9r2Me3ApyNvPXtwOmAVACs
vCRUAFRAtlpdbgdQgUMFZCtvSAekg7BAMB4jOXocBQM+M5lJZI2k/3zn6OUku90O
P8EYfoiNQkYhkexIV5gkSVCjTZWfAbgAULSKNwD+A1hyIbch7esmqERb6CmkqfIU
QN0R0LUNgDkXck0phDq09okAgAxArUQPL0eaKr+2EPFuKYBbSiEkoaW8+6wGqAul
1PBsyoXcDCm0FoaBzrmQUy7kHwCXmudXgyHSQ2PJhbzs1K0BlJ16My7kaghETDQe
NGWPsahQCDk1lOsSf6IpK5sqL79UiFr4sp6ZyUYcCZVQIn0DmDi0E0yFRaJh8n7h
6ZSoRHw6TmNRYZFooDvNWm5Jrg5NxKfDwjJXTg4ixJLGe93MY0y3hyLiQiNzJAIA
mXJWPCEuNBwTPTh0XYmcOtZvJ/yxC0lXKsyBRqnZ8MEzzMipsJi4HSkEUWGRaWSm
dYXaeSwiDQrLmipfkAgJzI0yYD35cKLNroAdiEaIkFQd3vyPuopGHShiqsj8C2hj
qy4qtr5HXQoaNcKvfvZSMRJpqrwA8ITh2AcVVyIXGJb1UmE9u9Yzgs7vAfxVn3uC
9ozOPbI5FHnaigs5b/2eG+61nKiokF/bhlZGIOTRsoxkFx3zRc+EeO/ltSCuCNpe
tC8T1HeKvFu7Tr8vjoehfR0XRHk3c51+b4ioF0QiesdkFMKFXAK4G9A6ct53a9+b
7FzIc+WF7RcK2ODtPcud96axlagpgBOiadkpv7iQnxZS67e639HGfz6MQkYhv0TI
6wAyvcsVoGa//gAAAABJRU5ErkJggg=="/>

    <h1>Access Denied</h1>

    <p>
    You are not authorized to access this application.  Would you like to log out and log in as another user?
    </p>

    <p style="text-align: center">
        <button class="darkBlueButton" onclick="window.location.replace('${logout_url}?reason=unauthorized')">Yes</button>
        <button class="darkBlueButton" onclick="window.location.replace('http://www.cyverse.org')">No</button>
    </p>

    <hr />

    <div style="float: right"><a href="http://www.cyverse.org">CyVerse Home Page</a></div>

    </div>

</body>
</html>
