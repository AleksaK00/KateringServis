-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 26, 2025 at 05:48 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kateringservis`
--

-- --------------------------------------------------------

--
-- Table structure for table `artikal`
--

CREATE TABLE `artikal` (
  `id` int(11) NOT NULL,
  `cena` double NOT NULL,
  `kategorija` varchar(255) DEFAULT NULL,
  `naziv` varchar(255) DEFAULT NULL,
  `opis` varchar(255) DEFAULT NULL,
  `na_akciji` bit(1) NOT NULL,
  `na_prodaji` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `artikal`
--

INSERT INTO `artikal` (`id`, `cena`, `kategorija`, `naziv`, `opis`, `na_akciji`, `na_prodaji`) VALUES
(1, 3570, 'SLANO', 'Kanapei sa suhomesnatim', 'Po 8 komada: kanapei sa šunkom i gaudom, kanapei sa zlatiborskom pečenicom, kanapei sa goveđom pršutom, kanapei sa kulenom na urnebesu.', b'1', b'1'),
(2, 3300, 'SLANO', 'Kanapei bez mesa', 'Po 8 komada: kanapei sa gorgonzolom, orahom i pistaćima, kanapei sa jajetom prepelice na urnebesu, kanapei sa pohovanom maslinom, kanapei „Caprese“ sa mocarelom i cherry paradajzom.', b'0', b'1'),
(3, 4500, 'SLANO', 'Slani rolati i slana torta', 'Po 8 komada: rolat od spanaća, rolat Urnebes sa kulenom, rolat od ajvara sa šunkom, slana torta.', b'0', b'1'),
(4, 2670, 'SLANO', 'Miks peciva', 'Ukupno 1kg: mini pice, projice, koktel štapići, kiflice sir, kiflice šunka sir.', b'0', b'1'),
(5, 4350, 'SLANO', 'Suhomesnati miks 1', 'Ukupno 800g: Njeguški pršut, zlatiborska pečenica, domaći kulen, dimljeni vrat, kačkavalj, gorgonzola, feta, ementaler, čeri, masline.', b'0', b'1'),
(6, 3750, 'SLANO', 'Suhomesnati miks 2', 'Ukupno 800g: goveđa pršuta, zlatiborska pečenica, domaći kulen i budimska, dimljeni vrat, sirevi, koštunjavo i suvo voće, čeri, masline.', b'0', b'1'),
(7, 4260, 'SLANO', 'Miks pršuta i sirevi', 'Ukupno 1kg: Nekoliko vrsta sireva i njeguški pršut.', b'0', b'1'),
(8, 4260, 'SLANO', 'Tanjir sireva', 'Ukupno 1,5kg: više vrsta sireva sa krekerima, voćem i povrćem i orašastim plodovima.', b'1', b'1'),
(9, 5760, 'SLANO', 'Salate u čašama', 'Po 6 komada: grčka salata, cezar salata, italijanska salata, meksička salata.', b'0', b'1'),
(10, 2700, 'SLATKO', 'Sitni kolači miks', 'Ukupno 1kg: medena kocka,  figaro štangle, bele rum bombice, padobranci od kokosa, čoko vanilice, rozen kocke, trouglovi, bela bajadera, bajadera.\r\n', b'0', b'1'),
(11, 2700, 'SLATKO', 'Posni sitni kolači miks', 'Ukupno 1kg:  medena kocka, posni rolat od kokosa, posne rum bombice, kuglice od suvog voća, čoko vanilice, rozen kocke, zrna kafe, bajadera\r\n', b'0', b'1'),
(12, 4860, 'SLATKO', 'Makaron miks', '54 komada različitih ukusa makaron kolačića.', b'1', b'1'),
(13, 7200, 'SLATKO', 'Desert čašice miks', 'Po 6 komada: profiterole, plazma malina, dubai, tiramisu.', b'0', b'1'),
(14, 9550, 'SET', 'Slano - Slatko mali set', '1x kanapei bez mesa, 1x suhomesnati miks 1, 1x sitni kolači miks', b'0', b'1'),
(15, 19000, 'SET', 'Slano - Slatko veliki set', '1x kanapei sa suhomesnatim, 1x tanjir sireva, 1x suhomesnati miks 2, 1x slani rolati i slana torta, 1x makaron miks, 1x sitni kolači miks', b'0', b'1'),
(16, 10850, 'SET', 'Slatki set', '1x makaron miks, 1x desert čašice miks', b'0', b'1'),
(17, 12790, 'SET', 'Slani set', '1x miks peciva, 1x miks pršuta i sirevi, 1x salate u čašama, 1x kanapei bez mesa', b'0', b'1'),
(18, 1200, 'PROSLAVA', 'Cena proslave po osobi', 'Cena proslave po osobi', b'0', b'1');

-- --------------------------------------------------------

--
-- Table structure for table `korisnik`
--

CREATE TABLE `korisnik` (
  `id` int(11) NOT NULL,
  `adresa` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `ime` varchar(255) DEFAULT NULL,
  `je_obrisan` bit(1) NOT NULL,
  `korisnicko_ime` varchar(255) DEFAULT NULL,
  `prezime` varchar(255) DEFAULT NULL,
  `sifra` varchar(255) DEFAULT NULL,
  `uloga` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `korisnik`
--

INSERT INTO `korisnik` (`id`, `adresa`, `email`, `ime`, `je_obrisan`, `korisnicko_ime`, `prezime`, `sifra`, `uloga`) VALUES
(3, 'Adresa 123', 'aleksa56522@its.edu.rs', 'Aleksa', b'0', 'AleksaK', 'Karamarkovic', '$2a$10$2V4UhlrCw2KHGHLmwQYKbeUu8/OA2NXNEt1UOToU5gIsqxK2Pf3d2', 'KORISNIK'),
(4, 'Nova Adresa 123', 'noviKorisnik@gmail.com', 'Novi', b'0', 'NoviKorisnik', 'Korisnik', '$2a$10$Wnhzoh05e8/76qbHERliA.pudHBArTzOp4q9wGSQKj6ViJ5PrP.7m', 'KORISNIK'),
(7, 'Admin Adresa 123', 'admin1@kateringservis.com', 'Admin', b'0', 'Admin1', 'Administratorovic', '$2a$10$LHGYSGZn52FoNVBB.DANAeuXyeHue2LFSEl6Npw/fejrMKQ.L/I2K', 'ADMIN'),
(8, 'Menadzerska Ulica 3', 'Menadzer1@karetingservis.com', 'Menadzer', b'0', 'Menadzer1', 'Menadzer', '$2a$10$VQJD5qkfC38LwH/fnvqIk.OaSbiRmoXwBzYrHceKjsG9SucGV5BTG', 'MENADZER'),
(11, 'Perin penthaus 1', 'pera@yahoo.com', 'Pera', b'0', 'PeraPeric', 'Peric', '$2a$10$HR3s1umZabrrfpKvW1NR1O2mpc5AplbQEjp88AqfgBnuTs8eUcmeW', 'KORISNIK'),
(14, 'Lazina Adresa', 'email@gmail.com', 'Laza', b'1', 'AdminomDodatKorisnik', 'Lazić', '$2a$10$sSCMRuJ8PjpbEayjp4OQ8Of1613IpaAbBtuMPwzlfNHGQaESB.TOW', 'KORISNIK'),
(15, 'Adresa 33', 'menadzer2@gmail.com', 'Dejan', b'0', 'Menadzer2', 'Dejanović', '$2a$10$xvza4O5FLbW4/G0.gLTT7OkC3q4EckgNk80k5J8DJE5tXjW5.Wvhe', 'MENADZER');

-- --------------------------------------------------------

--
-- Table structure for table `narudzbina`
--

CREATE TABLE `narudzbina` (
  `id` int(11) NOT NULL,
  `adresa` varchar(255) DEFAULT NULL,
  `datum` datetime(6) DEFAULT NULL,
  `korisnik_id` int(11) DEFAULT NULL,
  `cena` double NOT NULL,
  `otkazana` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `narudzbina`
--

INSERT INTO `narudzbina` (`id`, `adresa`, `datum`, `korisnik_id`, `cena`, `otkazana`) VALUES
(1, 'Adresa 123', '2025-05-07 20:00:00.000000', 3, 16260, b'0'),
(2, 'Nova Adresa 123', '2025-05-21 14:00:00.000000', 4, 19000, b'0'),
(3, 'Neka nova adresa', '2025-05-29 20:00:00.000000', 3, 16290, b'0'),
(4, 'Adresa treca', '2025-06-27 20:00:00.000000', 3, 13830, b'1'),
(5, 'Adresa 123', '2025-05-08 15:00:00.000000', 3, 19000, b'1'),
(6, 'Perin penthaus 1', '2025-05-22 10:00:00.000000', 11, 19800, b'1'),
(7, 'Nova Adresa 123', '2025-05-30 15:00:00.000000', 4, 10710, b'0'),
(8, 'Nova Adresa 123', '2025-05-23 12:00:00.000000', 4, 16680, b'1'),
(9, 'Adresa 123', '2025-05-29 10:00:00.000000', 3, 6426, b'0'),
(10, 'Adresa 123', '2025-05-29 20:00:00.000000', 3, 12582, b'0');

-- --------------------------------------------------------

--
-- Table structure for table `poruka`
--

CREATE TABLE `poruka` (
  `id` int(11) NOT NULL,
  `vreme` datetime(6) DEFAULT NULL,
  `proslava_id` int(11) DEFAULT NULL,
  `ime_posaljioca` varchar(255) DEFAULT NULL,
  `sadrzaj` longtext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `poruka`
--

INSERT INTO `poruka` (`id`, `vreme`, `proslava_id`, `ime_posaljioca`, `sadrzaj`) VALUES
(2, '2025-05-11 17:40:52.771692', 2, 'PeraPeric', 'Proba proba'),
(3, '2025-05-11 18:01:41.000000', 2, 'Menadžer', 'Proba odgovor menadžera'),
(4, '2025-05-17 19:42:13.998091', 2, 'PeraPeric', 'Hej, možete da se fokusirate na mediteransku hranu?'),
(5, '2025-05-17 19:43:12.000000', 2, 'Menadžer', 'Važi, zapisano.'),
(6, '2025-05-20 17:30:53.124213', 1, 'AleksaK', 'Test\r\n123'),
(7, '2025-05-24 14:07:54.928510', 2, 'Menadzer1', 'Proba menadzer poruka');

-- --------------------------------------------------------

--
-- Table structure for table `proslava`
--

CREATE TABLE `proslava` (
  `id` int(11) NOT NULL,
  `adresa` varchar(255) DEFAULT NULL,
  `br_gostiju` int(11) NOT NULL,
  `datum` datetime(6) DEFAULT NULL,
  `napomena` varchar(255) DEFAULT NULL,
  `korisnik_id` int(11) DEFAULT NULL,
  `cena` double NOT NULL,
  `otkazana` bit(1) NOT NULL,
  `neprocitana_poruka_korisnik` bit(1) NOT NULL,
  `neprocitana_poruka_menadzer` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `proslava`
--

INSERT INTO `proslava` (`id`, `adresa`, `br_gostiju`, `datum`, `napomena`, `korisnik_id`, `cena`, `otkazana`, `neprocitana_poruka_korisnik`, `neprocitana_poruka_menadzer`) VALUES
(1, 'Adresa Dogadjaja 12', 90, '2025-07-17 16:00:00.000000', 'Napomena 123\r\nIzmenjen broj gostiju, zatražena mediteranska kuhinja', 3, 100000, b'0', b'0', b'0'),
(2, 'Perin penthaus 1', 150, '2025-06-18 22:22:00.000000', '', 11, 148500, b'0', b'0', b'0'),
(3, 'Neka Adresa 123', 60, '2025-05-22 20:00:00.000000', 'Napomena 123', 11, 66000, b'1', b'0', b'0'),
(4, 'Neka Adresa 123', 80, '2025-10-24 14:00:00.000000', 'Za vencanje', 4, 88000, b'1', b'0', b'0'),
(5, 'Adresa Dogadjaja 12', 150, '2025-05-30 12:00:00.000000', '', 4, 148500, b'0', b'0', b'0'),
(6, 'Adresa Dogadjaja 12', 130, '2025-05-29 12:00:00.000000', '', 4, 128700, b'1', b'0', b'0'),
(7, 'Nova Proslava Adresa 123', 100, '2025-05-30 20:00:00.000000', 'Višelinijska napomena za proveru ispisa u tabeli.\r\nLinija 1\r\nParagraf Paragraf Paragraf Paragraf Paragraf Paragraf Paragraf Paragraf Paragraf Paragraf Paragraf Paragraf Paragraf Paragraf Paragraf Paragraf Paragraf Paragraf Paragraf Paragraf Paragraf', 3, 99000, b'0', b'0', b'0');

-- --------------------------------------------------------

--
-- Table structure for table `stavka`
--

CREATE TABLE `stavka` (
  `id` int(11) NOT NULL,
  `kolicina` int(11) NOT NULL,
  `artikal_id` int(11) DEFAULT NULL,
  `narudzbina_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `stavka`
--

INSERT INTO `stavka` (`id`, `kolicina`, `artikal_id`, `narudzbina_id`) VALUES
(1, 2, 1, 1),
(2, 1, 8, 1),
(3, 1, 12, 1),
(4, 1, 15, 2),
(5, 1, 1, 3),
(6, 1, 8, 3),
(7, 1, 9, 3),
(8, 1, 11, 3),
(9, 1, 4, 4),
(10, 1, 9, 4),
(11, 2, 11, 4),
(12, 1, 15, 5),
(13, 2, 13, 6),
(14, 2, 10, 6),
(15, 3, 4, 7),
(16, 1, 10, 7),
(17, 1, 2, 8),
(18, 2, 8, 8),
(19, 1, 12, 8),
(20, 2, 1, 9),
(21, 1, 8, 10),
(22, 2, 12, 10);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `artikal`
--
ALTER TABLE `artikal`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `korisnik`
--
ALTER TABLE `korisnik`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `narudzbina`
--
ALTER TABLE `narudzbina`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKtg9q36hdtsjm69jscig82lir7` (`korisnik_id`);

--
-- Indexes for table `poruka`
--
ALTER TABLE `poruka`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK6sg5m5art14ux0tixop65cytl` (`proslava_id`);

--
-- Indexes for table `proslava`
--
ALTER TABLE `proslava`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKkam4gq6ei4rdpiuxu7mnjepfu` (`korisnik_id`);

--
-- Indexes for table `stavka`
--
ALTER TABLE `stavka`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK4wxgf1cwjcxmxphabw1m6x8y` (`artikal_id`),
  ADD KEY `FKg62v50de2uq9ei8mj0y8ik32b` (`narudzbina_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `artikal`
--
ALTER TABLE `artikal`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `korisnik`
--
ALTER TABLE `korisnik`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `narudzbina`
--
ALTER TABLE `narudzbina`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `poruka`
--
ALTER TABLE `poruka`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `proslava`
--
ALTER TABLE `proslava`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `stavka`
--
ALTER TABLE `stavka`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `narudzbina`
--
ALTER TABLE `narudzbina`
  ADD CONSTRAINT `FKtg9q36hdtsjm69jscig82lir7` FOREIGN KEY (`korisnik_id`) REFERENCES `korisnik` (`id`);

--
-- Constraints for table `poruka`
--
ALTER TABLE `poruka`
  ADD CONSTRAINT `FK6sg5m5art14ux0tixop65cytl` FOREIGN KEY (`proslava_id`) REFERENCES `proslava` (`id`);

--
-- Constraints for table `proslava`
--
ALTER TABLE `proslava`
  ADD CONSTRAINT `FKkam4gq6ei4rdpiuxu7mnjepfu` FOREIGN KEY (`korisnik_id`) REFERENCES `korisnik` (`id`);

--
-- Constraints for table `stavka`
--
ALTER TABLE `stavka`
  ADD CONSTRAINT `FK4wxgf1cwjcxmxphabw1m6x8y` FOREIGN KEY (`artikal_id`) REFERENCES `artikal` (`id`),
  ADD CONSTRAINT `FKg62v50de2uq9ei8mj0y8ik32b` FOREIGN KEY (`narudzbina_id`) REFERENCES `narudzbina` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
