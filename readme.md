<h2>Ore Discovery Broadcast</h2>
<p><a href="https://github.com/Serilum/Ore-Discovery-Broadcast"><img src="https://serilum.com/assets/data/logo/ore-discovery-broadcast.png"></a></p><h2>Download</h2>
<p>You can download Ore Discovery Broadcast on CurseForge and Modrinth:</p><p>&nbsp;&nbsp;CurseForge: &nbsp;&nbsp;<a href="https://curseforge.com/minecraft/mc-mods/ore-discovery-broadcast">https://curseforge.com/minecraft/mc-mods/ore-discovery-broadcast</a><br>&nbsp;&nbsp;Modrinth: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="https://modrinth.com/mod/ore-discovery-broadcast">https://modrinth.com/mod/ore-discovery-broadcast</a></p>
<h2>Issue Tracker</h2>
<p>To keep a better overview of all mods, the issue tracker is located in a separate repository.<br>&nbsp;&nbsp;For issues, ideas, suggestions or anything else, please follow this link:</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;-> <a href="https://github.com/ricksouth/serilum-mc-mods/issues">Issue Tracker</a></p>
<h2>Pull Requests</h2>
<p>Because of the way mod loader files are bundled into one jar, some extra information is needed to do a PR.<br>&nbsp;&nbsp;A wiki page entry about it is available here:</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;-> <a href="https://github.com/ricksouth/serilum-mc-mods/wiki/Pull-Request-Information">Pull Request Information</a></p>
<h2>Mod Description</h2>
<p><a href="https://serilum.com/" rel="nofollow"><img src="https://github.com/ricksouth/serilum-mc-mods/raw/master/description/a1.jpg" alt="" width="838" height="400"></a><br><br><img src="https://github.com/ricksouth/serilum-mc-mods/raw/master/description/Versions/header.png"><a href="https://legacy.curseforge.com/minecraft/mc-mods/ore-discovery-broadcast/files/all?filter-status=1&filter-game-version=1738749986:75125" rel="nofollow"><img src="https://github.com/ricksouth/serilum-mc-mods/raw/master/description/Versions/1_20.png"></a><a href="https://legacy.curseforge.com/minecraft/mc-mods/ore-discovery-broadcast/files/all?filter-status=1&filter-game-version=1738749986:73407" rel="nofollow"><img src="https://github.com/ricksouth/serilum-mc-mods/raw/master/description/Versions/1_19.png"></a><br><br><strong><span style="font-size:24px">Requires the library mod&nbsp;<a style="font-size:24px" href="https://curseforge.com/minecraft/mc-mods/collective" rel="nofollow">Collective</a>.<br></span></strong></p>
<p><span style="font-size:18px">Ore Discovery Broadcast is a mod made specifically for server environments, but can still be used in singleplayer or LAN worlds. Whenever a player mines an ore, a message is broadcasted. With a configurable delay in between messages from the same ore. The format and colour of the message can be changed. It's also possible to choose which ores should be broadcasted, and which shouldn't, via the blacklist file.<br><br>Allows for more interaction between players, seeing what others are doing and which ores they find. The mod is also a viable solution as xray-prevention. As it's very noticable when a player keeps finding diamonds in an unnatural way. By default, the functionality is only enabled in dedicated servers. Can be changed by setting '<em><strong>onlyRunOnDedicatedServers</strong></em>' in the config to <em><strong>false</strong></em>. Placed ore blocks by players will not be broadcasted.<br></span><br><br><strong><span style="font-size:20px">Configurable:</span> <span style="color:#008000;font-size:14px"><a style="color:#008000" href="https://github.com/ricksouth/serilum-mc-mods/wiki/how-to-configure-mods" rel="nofollow">(&nbsp;how do I configure?&nbsp;)</a></span><br></strong><span style="font-size:12px"><strong>onlyRunOnDedicatedServers</strong>&nbsp;(default = true): If the mod should only run on dedicated servers. When enabled it's not sent when in a singleplayer world.</span><br><span style="font-size:12px"><strong>ignorePlacedOreBlocks</strong>&nbsp;(default = true): If ore blocks placed by players should be ignored for broadcasts.</span><br><span style="font-size:12px"><strong>tickDelayBetweenSameOreBroastcasts</strong>&nbsp;(default = 600, min 0, max 72000): How many ticks in between ore discoveries before another broadcast is sent. Resets when the same block is mined. 20 ticks = 1 second</span><br><span style="font-size:12px"><strong>messageFormat</strong>&nbsp;(default = "%player% has found %ore%!"): The format of the broadcasted message. %player% = player name, %ore% = ore name</span><br><span style="font-size:12px"><strong>lowercaseOreName</strong>&nbsp;(default = true): Whether the ore name should be displayed in lowercase characters.</span><br><span style="font-size:12px"><strong>addOreCountToMessage</strong>&nbsp;(default = false): If the broadcasted message should contain how big the ore vein is. Will be included in %ore%.</span><br><span style="font-size:12px"><strong>hideDeepslateFromName</strong>&nbsp;(default = true): Whether the ore name should have 'deepslate' hidden if it exists.</span><br><span style="font-size:12px"><strong>ignoreCreativePlayers</strong>&nbsp;(default = true): If enabled, ore discoveries won't be announced when a player is in creative mode.</span><br><span style="font-size:12px"><strong>ignoreFakePlayers</strong>&nbsp;(default = true): If enabled, ore discoveries won't be announced when it is broken by a simulated fake player.</span><br><br></p>
<p><br><span style="font-size:24px"><strong>Ore Blacklist</strong></span><br><span style="font-size:14px">There is a <em><strong>blacklist.txt</strong></em> file available in the folder <em><strong>./config/orediscoverybroadcast/</strong></em>. Here you can specify which ores shouldn't be broadcasted when found, by adding an exclamation mark (!) in front of the line. Quartz is excluded by default:</span><br><picture><img src="https://github.com/ricksouth/serilum-mc-mods/raw/master/cdn/ore-discovery-broadcast/a.png"><br><br><span style="font-size:24px"><strong>Broadcast message colour config</strong></span><br><span style="font-size:14px">Inside <em><strong>./config/orediscoverybroadcast/.</strong></em> there is also a file called <em><strong>colourmap.txt</strong></em>. Here you can specify what colour should be set for specific ores. Contains default values.<br>Ores from other mods will also be shown, with the default colour blue (9):<br><br></span><img src="https://github.com/ricksouth/serilum-mc-mods/raw/master/cdn/ore-discovery-broadcast/a1.png"></picture><br><br><br><span style="font-size:24px"><strong>Example GIFs</strong></span><br><span style="font-size:14px">Here you can see the message being broadcast to the server. Mining multiple of the same ores only shows one message. Mining different ores shows multiple:</span></p>
<div class="spoiler">
<p><picture><img src="https://github.com/ricksouth/serilum-mc-mods/raw/master/cdn/ore-discovery-broadcast/b.gif"></picture></p>
</div>
<p>&nbsp;<br><span style="font-size:14px">Another example with mining multiple coal ore:</span></p>
<div class="spoiler">
<p><picture><img src="https://github.com/ricksouth/serilum-mc-mods/raw/master/cdn/ore-discovery-broadcast/c.gif"></picture></p>
</div>
<p>&nbsp;<br><span style="font-size:14px">Here the message format has been changed in the config. The content and colour is configurable:</span></p>
<div class="spoiler">
<p><picture><img src="https://github.com/ricksouth/serilum-mc-mods/raw/master/cdn/ore-discovery-broadcast/d.gif"></picture></p>
</div>
<p>&nbsp;<br><span style="font-size:14px">A different colour can be set for each ore:</span></p>
<div class="spoiler">
<p><picture><img src="https://github.com/ricksouth/serilum-mc-mods/raw/master/cdn/ore-discovery-broadcast/g.gif"></picture></p>
</div>
<p>&nbsp;<br><span style="font-size:14px">Here's an example with the '<strong><em>addOreCountToMessage</em></strong>' config option enabled. The broadcasted message will contain how many ores are included in the vein:</span></p>
<div class="spoiler">
<p><picture><img src="https://github.com/ricksouth/serilum-mc-mods/raw/master/cdn/ore-discovery-broadcast/f.gif"></picture></p>
</div>
<p>&nbsp;</p>
<p><br>------------------<br><br><span style="font-size:24px"><strong>You may freely use this mod in any modpack, as long as the download remains hosted within the CurseForge or Modrinth ecosystem.</strong></span><br><br><span style="font-size:18px"><a style="font-size:18px;color:#008000" href="https://serilum.com/" rel="nofollow">Serilum.com</a> contains an overview and more information on all mods available.</span><br><br><span style="font-size:14px">Comments are disabled as I'm unable to keep track of all the separate pages on each mod.</span><span style="font-size:14px"><br>For issues, ideas, suggestions or anything else there is the&nbsp;<a style="font-size:14px;color:#008000" href="https://github.com/ricksouth/serilum-mc-mods/" rel="nofollow">Github repo</a>. Thanks!</span><span style="font-size:6px"><br><br></span><a href="https://ricksouth.com/donate" rel="nofollow"><img src="https://raw.githubusercontent.com/ricksouth/serilum-mc-mods/master/description/Shields/donation_rounded.svg" alt="" width="306" height="50"></a></p>