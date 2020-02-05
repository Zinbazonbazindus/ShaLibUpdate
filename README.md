# ShaLibUpdate
Un système de téléchagement et mise à jour simple. Dans cette librarie se trouve 2 modes de fonctionnement. Commençons par découvrir ce que contient la librarie et comment l'utiliser...

**Sachez avant que je ne suis qu'un développeur débutant, donc mon code n'est pas forcément le meilleur et le plus optimisé. Merco de votre compréhension.**

### Utilisation
## Côté WEB — L'endroit ou vous stockerez vos fichiers pour les télécharger.
C'est très simple. Tu as juste à ouvrir le fichier __webside.zip__ sur ton pc, puis glisses le contenu sur ta machine web / hébergement web. Si tout se passe bien tu devrais avoir un dossier __files__ et un fichier __informations.json__, et un fichier __index.php__ dans le dossier __files__. Le dossier __files__ sert quant à lui à mettre tes fichiers qui seront téléchargés par la lib. Tu peux donc glisser les fichiers que tu souhaites dedans.
# Ensuite
Tu vas devoir éditer le fichier __informations.json__ pour le configurer. Le contenu de ce fichier devrait être :

 `{
    "updater": {
        "syncFolders": "",
        "filesFolderUrl": "link",
        "enabled": "true"
      }
 }`
 
Pour commencer, un dossier peut être synchronisé en ajoutant son nom entre les "" à coté de __syncFolders__, c'est à dire que le contenu que tu rajouteras dedans sera téléchargé __seulement si__ il n'est pas présent pas dans le dossier sur ton pc, et le contenu supprimé du serveur web le sera aussi dans ton dossier sur ton pc. Pour en ajouter plusieurs, mets simplement une , entre les noms de dossiers.

Ensuite, tu devras récuprérer le lien qui mène au fichier __informations.json__ sur ton serveur web et le mettre dans les "" à coté de __filesFolderUrl__.

Enfin, le __"enabled": "true"__ signifie que ton serveur de mise à jour est "allumé". Si tu veux "l'éteindre", tu devras __remplacer true par false__.

## Premier mode — Le mode Minecraft.
Ce mode te permet d'indiquer le nom du dossier que tu veux, et qui sera crée dans le %appdata% (la ou se situe le .minecraft). Pour mettre un nom de dossier, remplace __foldername__ par ce que tu veux ci dessous.
Comme expliqué plus haut, __link of the informations.json__ est juste le lien de ton fichier __informations.json__, donc pour mettre ton lien tu n'a qu'a remplacer __link of the informations.json__ par ton lien.

__Code :__
* ShaMain.ShaUpdaterMC("foldername", "link of the informations.json");

## Deuxième mode — Le mode "autre".
Ce mode te permet de mettre le chemin d'accès que tu veux. Si le dossier n'existe pas il sera automatiquement crée. Pour mettre le chemin d'accès que tu veux, remplace __folderpath__ par le chemin souhaité, et remplace __link of the informations.json__ par le lien de ton __informations.json__.

__Code :__
* ShaMain.ShaUpdaterOTHER("folderpath", "link of the informations.json");

## Informations récupérables durant le téléchargement".
Tu peux récupérer ces informations :
* ShaMain.globalprogress (int) //Permet de récup le pourcentage global
* ShaMain.fileprogress (int) //Permet de récup le pourcentage du fichier en cours de téléchargement
* ShaMain.currentDownloadLink (String) //Permet de récup le lien du fichier en cours de téléchargement
* ShaMain.downloadFinish (boolean) //Permet de savoir si le téléchargement est fini.
* ShaMain.downloadStarted (boolean) //Permet de savoir si le téléchargement à débuté.
* ShaMain.downloadSpeed (String) //Permet de récupérer la vitesse de téléchargement (en ko/s ou mb/s - l'unité se met automatiquement à jour)
* ShaMain.remoteUrl (String) //Permet de récupérer l'URL du fichier __informations.json__ mit dans le code.
* ShaMain.updaterenabled (Boolean) //Permet de récupérer l'état du serveur de mise a jour (ligne enabled dans informations.json).

 - Evidemment pour récupérer certaines informations tu vas devoir faire une boucle. Exemple :
 
`	while(ShaMain.downloadFinish == false) { //Si le téléchargement n'est pas fini\n
		if (ShaMain.downloadStarted = true) { //Si le téléchargement à commencé\n
  			 System.out.println(ShaMain.fullprogress); //Print la progression globale.\n
			}\n
		}\n

`
 Tu es libre de ton code, donc tu fais ce que tu veux avec ^^.
