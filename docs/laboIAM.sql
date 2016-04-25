-- phpMyAdmin SQL Dump
-- version 4.2.12deb2+deb8u1
-- http://www.phpmyadmin.net
--
-- Client :  localhost
-- Généré le :  Lun 25 Avril 2016 à 13:39
-- Version du serveur :  5.5.47-0+deb8u1
-- Version de PHP :  5.6.19-0+deb8u1


-- -----------------------------------------------------
-- Schema laboIAM
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `laboIAM` ;

-- -----------------------------------------------------
-- Schema laboIAM
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `laboIAM` DEFAULT CHARACTER SET utf8 ;
USE `laboIAM` ;


SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `laboIAM`
--

-- --------------------------------------------------------

--
-- Structure de la table `action`
--

CREATE TABLE IF NOT EXISTS `action` (
`action` int(11) NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT 'Not Defined',
  `Description` varchar(2048) NOT NULL DEFAULT 'Not Defined'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `agenda`
--

CREATE TABLE IF NOT EXISTS `agenda` (
  `dayId` int(11) NOT NULL,
  `roomId` int(11) NOT NULL,
  `action` int(11) NOT NULL,
  `hours` time DEFAULT '00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `lightSensorMonitoring`
--

CREATE TABLE IF NOT EXISTS `lightSensorMonitoring` (
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `outsideLight` int(11) DEFAULT '0',
  `insideLight` int(11) DEFAULT '0',
  `roomId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `parameter`
--

CREATE TABLE IF NOT EXISTS `parameter` (
  `key` varchar(64) NOT NULL,
  `param` varchar(64) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `room`
--

CREATE TABLE IF NOT EXISTS `room` (
`roomId` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` varchar(2048) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Contenu de la table `room`
--

INSERT INTO `room` (`roomId`, `name`, `description`) VALUES
(1, 'Cuisine', 'La cuisine est une pièce spécifique dans un bâtiment, spécialement équipée pour la préparation des aliments et des plats. On y dispose généralement de l''eau courante et divers appareils électroménagers (cuisinière, four, réfrigérateur...). La cuisine peut disposer de locaux annexes : les celliers et fruitiers des habitations, les chambres froides de restaurants, d''hôtels et d''usines de cuisine industrielle. Dans un logement de type studio la cuisine n''est pas une pièce mais un équipement le long d''un mur.');

-- --------------------------------------------------------

--
-- Structure de la table `spice`
--

CREATE TABLE IF NOT EXISTS `spice` (
`spiceId` int(11) NOT NULL,
  `name` varchar(45) NOT NULL DEFAULT 'Not defined',
  `description` varchar(10000) NOT NULL DEFAULT 'Not available',
  `bareCode` varchar(10) DEFAULT 'Empty'
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

--
-- Contenu de la table `spice`
--

INSERT INTO `spice` (`spiceId`, `name`, `description`, `bareCode`) VALUES
(1, 'Muscade', 'La noix de muscade est utilisée râpée comme épice pour aromatiser les viandes, soupes, béchamels, gratins, assaisonner purées et plats à base de pommes de terre en général, légumes et même certains cocktails. Elle entre dans la composition de nombreux mélanges d’épices tel que le Curry, le quatre épices, ou encore le raz el hanout et le colombo. En cuisine française, elle est notamment employée pour aromatiser le gratin dauphinois. Le goût de la muscade est assez fort mais non piquant. Le plus souvent, une pincée suffira. Si vous aimez cette épice, nous vous conseillons de tester le macis, qui est l''écorce de la muscade, possédant un goût très similaire mais en poivré, citronné et beaucoup plus fin. Si vous n''avez plus de muscade ni de macis, vous pourrez employer les fèves tonka à la place, leur goût est différent mais dans le salé, elles s''utilisent de la même manière que les deux épices. ', '4a0012ee04'),
(2, 'Paprika', 'Le paprika est une épice en poudre de couleur rouge obtenu à partir du fruit mûr, séché et moulu du piment doux ou poivron. L''épice est utilisée en cuisine pour son parfum âcre et sa couleur rouge. Son piquant est très doux et vous permet d’agrémenter vos plats d''une touche parfumée et légèrement épicée. Les Hongrois l''emploient en grande quantité que ce soit dans leurs goulaschs, leurs ragoûts ou encore leurs plats de volaille. Il se mariera parfaitement à vos ragoûts de viande et de volaille, dans les fromages frais, la crème, le poisson, la tomate, les oignons ou encore l’avocat. Le paprika est sucré, ne le faites pas trop cuire sinon il prendra un goût amer. ', '4a0012fa94'),
(3, 'Piment', 'Le piment entre dans de nombreux plats typiques du Pays Basque, par exemple : le Marmitako, la Piperade à l''Ibaïona, l''Axoa d''Espelette, etc. C’est un piment très parfumé dont l''usage s’étend à tous les plats. Excellent dans la ratatouille et tous les plats de tomates (crues ou cuites). Utilisez-le dans toutes les viandes, que ce soient les viandes blanches, le lapin, la viande de mouton, bœuf, etc. Une pincée en fin de cuisson suffira à aromatiser votre viande. Vous pouvez l''employer dans le porc et la charcuterie de manière générale. En accompagnement, il parfumera à merveille vos pâtes, riz et plats de pomme de terre. Il assaisonnera parfaitement vos œufs dans toutes leurs variantes. Il parfume les sauces, particulièrement aux produits laitiers tels que le yaourt nature, le lait de coco, le fromage blanc ou la crème fraîche. Dans le pays Basque, on confectionne la mayonnaise en remplaçant la moutarde par du piment d’Espelette. Il est aussi parfait pour épicer les poissons de toutes sortes, mais son mariage avec le brochet et la raie est particulièrement délicieux. Avec un piquant équivalant au poivre, vous pouvez l’intégrer dans vos salades de toutes sortes. Plus étonnant mais tout aussi bon, vous pouvez l’intégrer (une pincée) dans vos desserts, surtout les desserts à base de chocolat. Pour profiter au maximum de sa saveur, rajoutez-le en fin de cuisson ou même après la cuisson.', '5c005c8aeb'),
(4, 'Poivre', 'Le poivre est une épice obtenue à partir des baies de différentes espèces de poivriers. Seuls ont droit à l''appellation de « poivre » les fruits du poivrier noir qui donnent le poivre vert, blanc, rouge ou noir. Par analogie, d''autres épices qui proviennent de plantes bien différentes reprennent ce nom, mais ces faux poivres ont d''autres caractéristiques botaniques et donnent des saveurs différentes. Le poivre noir est l’une des épices les plus employées dans le monde. C’est le plus piquant et le plus aromatique des poivres. On l’ajoute pratiquement à tous les aliments et toutes les recettes : potages, viandes, légumes, vinaigrettes, sauces, etc. On rajoute toujours le poivre en fin de cuisson, une pincée suffit à relever subtilement un plat. Un usage plus surprenant consiste à l''utiliser pour aromatiser des thés et des infusions.', '5c005cb6b9'),
(5, 'Réglisse', 'Le réglisse sert principalement à donner du goût dans les pâtisseries et les boissons. Sa racine peut aussi se déguster tel quel en bouche, à l''image d''une friandise. En Asie, on l''utilise pour adoucir les soupes et dans certains mélanges d’épices. Le réglisse est excellent saupoudré sur du poulet. Son usage est très répandu dans la cuisine thaïlandaise. Vous pouvez vous en servir pour aromatiser vos thés et infusions.', '5c005cc39b'),
(6, 'Vanille', 'Coupez dans le sens de la longueur votre gousse afin d''en extraire les graines. Vous n''êtes pas obligé de l''ouvrir sur toute sa longueur, quelques centimètres suffiront, vous pourrez ainsi la réutiliser à plusieurs reprises ! Séparez la gousse en 2 et raclez l''intérieur de cette demi-gousse à l''aide de la lame de votre couteau pour en extraire les graines. Les graines ainsi obtenues se mélangeront maintenant parfaitement à votre préparation. On peut récupérer les 2 demi-gousses vidées, soit pour parfumer votre bocal de sucre, soit pour les incorporer dans vos recettes mais en principe elles ne se mangent pas. Laissez chauffer les graines et le reste de la gousse une dizaine de minutes dans le lait sans porter à ébullition. Plus les graines resteront longtemps, plus la saveur sera bien imprégnée. La vanille sublime tous les desserts mais vous pouvez aussi vous en servir pour parfumer vos thés et infusions. Enfin, elle se marie très bien aux viandes blanches ainsi qu’aux préparations au lait de coco.', '5c005ce795');

-- --------------------------------------------------------

--
-- Structure de la table `spiceBox`
--

CREATE TABLE IF NOT EXISTS `spiceBox` (
`location` int(11) NOT NULL,
  `spiceId` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

--
-- Contenu de la table `spiceBox`
--

INSERT INTO `spiceBox` (`location`, `spiceId`) VALUES
(1, NULL),
(2, NULL),
(3, NULL),
(4, NULL),
(5, NULL),
(6, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `temperatureSensorMonitoring`
--

CREATE TABLE IF NOT EXISTS `temperatureSensorMonitoring` (
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `temperature` int(11) DEFAULT '0',
  `roomId` int(11) NOT NULL,
  `heatingState` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Index pour les tables exportées
--

--
-- Index pour la table `action`
--
ALTER TABLE `action`
 ADD PRIMARY KEY (`action`);

--
-- Index pour la table `agenda`
--
ALTER TABLE `agenda`
 ADD KEY `roomId3` (`roomId`), ADD KEY `actionId` (`action`);

--
-- Index pour la table `lightSensorMonitoring`
--
ALTER TABLE `lightSensorMonitoring`
 ADD PRIMARY KEY (`roomId`);

--
-- Index pour la table `parameter`
--
ALTER TABLE `parameter`
 ADD PRIMARY KEY (`key`), ADD UNIQUE KEY `key_UNIQUE` (`key`);

--
-- Index pour la table `room`
--
ALTER TABLE `room`
 ADD PRIMARY KEY (`roomId`);

--
-- Index pour la table `spice`
--
ALTER TABLE `spice`
 ADD PRIMARY KEY (`spiceId`), ADD UNIQUE KEY `spiceId_UNIQUE` (`spiceId`), ADD UNIQUE KEY `bareCode_UNIQUE` (`bareCode`);

--
-- Index pour la table `spiceBox`
--
ALTER TABLE `spiceBox`
 ADD PRIMARY KEY (`location`), ADD UNIQUE KEY `location_UNIQUE` (`location`), ADD KEY `spiceId_idx` (`spiceId`);

--
-- Index pour la table `temperatureSensorMonitoring`
--
ALTER TABLE `temperatureSensorMonitoring`
 ADD KEY `roomId_idx` (`roomId`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `action`
--
ALTER TABLE `action`
MODIFY `action` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `room`
--
ALTER TABLE `room`
MODIFY `roomId` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT pour la table `spice`
--
ALTER TABLE `spice`
MODIFY `spiceId` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT pour la table `spiceBox`
--
ALTER TABLE `spiceBox`
MODIFY `location` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=7;
--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `agenda`
--
ALTER TABLE `agenda`
ADD CONSTRAINT `actionId` FOREIGN KEY (`action`) REFERENCES `action` (`action`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `roomId3` FOREIGN KEY (`roomId`) REFERENCES `room` (`roomId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `lightSensorMonitoring`
--
ALTER TABLE `lightSensorMonitoring`
ADD CONSTRAINT `roomId1` FOREIGN KEY (`roomId`) REFERENCES `room` (`roomId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `spiceBox`
--
ALTER TABLE `spiceBox`
ADD CONSTRAINT `spiceId` FOREIGN KEY (`spiceId`) REFERENCES `spice` (`spiceId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `temperatureSensorMonitoring`
--
ALTER TABLE `temperatureSensorMonitoring`
ADD CONSTRAINT `roomId2` FOREIGN KEY (`roomId`) REFERENCES `room` (`roomId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
