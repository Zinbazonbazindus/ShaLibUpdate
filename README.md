# ShaLibUpdate
Un système de téléchagement et mise à jour simple. Cette librarie permet de mettre à jour, supprimer et télécharger les fichier et elle possède 2 modes de fonctionnement. Commençons par découvrir ce que contient la librarie et comment l'utiliser...

**Sachez avant que je ne suis qu'un développeur débutant, donc mon code n'est pas forcément le meilleur et le plus optimisé. Merco de votre compréhension.**

Téléchargement de la lib : https://github.com/Shawiizz/ShaLibUpdate/raw/master/ShaLibUpdate_0.2.jar
Téléchargement du fichier webside.zip : https://github.com/Shawiizz/ShaLibUpdate/raw/master/webside.zip

### Utilisation
## Côté WEB — L'endroit ou tu stockeras tes fichiers.
C'est très simple. Tu as juste à ouvrir le fichier __webside.zip__ sur ton pc, puis glisses le contenu sur ta machine web / hébergement web. Si tout se passe bien tu devrais avoir un dossier __files__ et un fichier __informations.json__, et un fichier __index.php__ dans le dossier __files__. Le dossier __files__ sert quant à lui à mettre tes fichiers qui seront téléchargés par la lib. Tu peux donc glisser les fichiers que tu souhaites dedans.
# Ensuite
Tu vas devoir éditer le fichier __informations.json__ pour le configurer. Le contenu de ce fichier devrait être :

![](https://image.noelshack.com/fichiers/2020/06/3/1580924086-capture1.png)
 
Pour commencer, un dossier peut être synchronisé en ajoutant son nom entre les "" à coté de __syncFolders__, c'est à dire que le contenu que tu rajouteras dedans sera téléchargé, ou alors remplacé si la taille est différente, et le(s) fichier(s) supprimé(s) du serveur web le sera(ont) aussi dans ton dossier sur ton pc. Pour ajouter plusieurs dossiers à synchroniser, mets simplement une , entre les noms des dossiers.

Ensuite, tu devras récuprérer le lien qui mène au fichier __informations.json__ sur ton serveur web et le mettre dans les "" à coté de __filesFolderUrl__. Garde le __précieusement__, tu en auras besoin plus tard.

Enfin, le __"enabled": "true"__ signifie que ton serveur de mise à jour est "allumé". Si tu veux "l'éteindre", tu devras __remplacer true par false__.

**Une whitelist est disponible pour autoriser des fichiers à être dans le dossier que vous avez défini.**
Pour l'activer, il te suffit de rajouter *"whiteList": "whitelistlink",* juste au dessus de *"enabled": "true"*. Une fois fait, crée un fichier __whitelist.json__ la ou se situe le __informations.json__, puis ajoutes comme sur ce screen.

![](https://zupimages.net/up/20/06/sqvu.png)

Ensuite tu devras télécharger ce logiciel : http://www.winmd5.com/download/winmd5free.zip pour obtenir le md5 de ton fichier (sa clé en gros). Tu décompresses le fichier winmd5free.zip, tu lances WinMD5.exe, tu cliques sur browse pour séléctionner ton fichier et ensuite tu copie la chaine de caractère qui est apparue, puis pour finir remplaces md5 par cette chaine de caractère. Tu peux mettre le nom que tu veux a la place de tonfichier. Si tu veux whitelist encore un autre fichier, pense à bien mettre une , derriere la 1ere ligne pour en ajouter une deuxieme en dessous, comme ceci :

![](https://zupimages.net/up/20/06/dl4r.png)

## Premier mode — Le mode Minecraft.
Ce mode te permet d'indiquer le nom du dossier que tu veux, et ce dossier sera crée automatiquement dans le %appdata% (la ou se situe le .minecraft). Pour mettre un nom de dossier, remplace __foldername__ par ce que tu veux ci dessous.
Comme expliqué plus haut, __link of the informations.json__ est juste le lien de ton fichier __informations.json__, donc pour mettre ton lien tu n'a qu'a remplacer __link of the informations.json__ par ton lien (voir code ci dessous).

__Code :__
* ShaMain.ShaUpdaterMC("foldername", "link of the informations.json");

## Deuxième mode — Le mode "autre".
Ce mode te permet de mettre le chemin d'accès que tu veux. Si le dossier n'existe pas il sera automatiquement crée. Pour mettre le chemin d'accès que tu veux, remplace __folderpath__ par le chemin souhaité, et remplace __link of the informations.json__ par le lien de ton __informations.json__.

__Code :__
* ShaMain.ShaUpdaterOTHER("folderpath", "link of the informations.json");

## Informations récupérables durant le téléchargement.
Tu peux récupérer ces informations :
* ShaMain.globalprogress (int) //Permet de récup le pourcentage global
* ShaMain.fileprogress (int) //Permet de récup le pourcentage du fichier en cours de téléchargement
* ShaMain.currentDownloadLink (String) //Permet de récup le lien du fichier en cours de téléchargement
* ShaMain.downloadFinish (boolean) //Permet de savoir si le téléchargement est fini.
* ShaMain.downloadStarted (boolean) //Permet de savoir si le téléchargement à débuté.
* ShaMain.downloadSpeed (String) //Permet de récupérer la vitesse de téléchargement (en ko/s ou mb/s - l'unité se met automatiquement à jour)
* ShaMain.remoteUrl (String) //Permet de récupérer l'URL du fichier __informations.json__ mit dans le code.
* ShaMain.updaterenabled (Boolean) //Permet de récupérer l'état du serveur de mise a jour (ligne enabled dans informations.json).

Evidemment pour récupérer certaines informations en temps réel comme le pourcentage tu vas devoir faire une boucle. Exemple :
 
![](https://image.noelshack.com/fichiers/2020/06/3/1580924086-capture2.png)

 Tu es libre de ton code, donc tu fais ce que tu veux avec ^^.
