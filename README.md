# LiveSplit Notes

LiveSplit Notes is a piece of software written with the goal of allowing speedrunners to view notes tied to specific splits within LiveSplit. The software allows you to utilise the [LivesSplit Server]([https://github.com/LiveSplit/LiveSplit.Server/releases](https://github.com/LiveSplit/LiveSplit.Server/releases)) to move through a text file. The text file can be split into sections through any String defined within the properties.

It was created with the purpose to allow new runners a simpler way to manage their notes and make it easier for them learn. This application replaces the need for physical notepad or having notepad.exe open and tabbing out to scroll.


## Configuration
![Config](https://i.imgur.com/75B8N8c.png)

By default, the application will point to the default port of the LivesSplit server and assume that it is running on the local machine. 
The application uses polling to determine the information from the server as there is currently no sort of notification system. The poll rate is measured in ms.
The back and forward keys are assigned to the forward and backwards arrows. The values that are shown within the text box are the key codes.
There are also 2 configuration settings that are not tied to the settings window. These are to do with the application itself. You are able to set the window to draw overtop of other windows and you can also enable global Key hooks. This means that the application will listen for the forwards and backwards keys when it is not in focus (in a way to mimic LiveSplit).

## Example Notes
Below is an extract of the notes that I am currently using to learn Portal 2. This will work with the default settings as it uses a blank line as a seperator.
```
sp_a1_intro1 - Container Ride
    For both skips, start walking forwards at "even in a dire e-*MER*-gen-cy"

sp_a1_intro2 - Portal Carousel
    "Because of the technical difficulties we are currently experiencing, your test environment is un-sup-er-*VIZED*.
    Before re-entering a *RE*-la-xa-tion vault at the conclusion of testing, please take a *MOMENT* to write down the results of your test. *(go inbetween 'test' and 'an')*
    An Aperture Science Reintegration *AS*-so-ci-ate will revive you for an inverview when society has been rebuilt."

sp_a1_intro3 - Portal Gun
    Slow dialogue skip:
        "I'm just going to *WAIT* for you up ahead."
    Fast dialogue skip:
        "That's important, *SHOULD* have asked that first."
    Elevator cue (not necessary if you get the dialogue skip):
        ".. in the *LAWS* of Robotics.."
        "*A* future Aperture Science Entitlement."
```

These will then be shown within the app like so:

![diplay](https://i.imgur.com/am7jWKC.png)

