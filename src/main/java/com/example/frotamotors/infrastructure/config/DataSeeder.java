package com.example.frotamotors.infrastructure.config;

import com.example.frotamotors.domain.enums.PartCategory;
import com.example.frotamotors.domain.enums.PartStatus;
import com.example.frotamotors.domain.enums.Role;
import com.example.frotamotors.domain.enums.VehicleStatus;
import com.example.frotamotors.domain.enums.VehicleType;
import com.example.frotamotors.domain.model.Part;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.infrastructure.persistence.PartRepository;
import com.example.frotamotors.infrastructure.persistence.UserRepository;
import com.example.frotamotors.infrastructure.persistence.VehicleRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class DataSeeder {

  private final UserRepository userRepository;
  private final VehicleRepository vehicleRepository;
  private final PartRepository partRepository;
  private final PasswordEncoder passwordEncoder;

  @Bean
  @Profile("!prod")
  public CommandLineRunner seedData() {
    return args -> {
      log.info("Starting data seeding...");

      // Criar utilizador de teste se não existir
      User testUser =
          userRepository
              .findByEmail("teste@frotamotors.com")
              .orElseGet(
                  () -> {
                    User user = new User();
                    user.setName("Utilizador Teste");
                    user.setEmail("teste@frotamotors.com");
                    user.setPasswordHash(passwordEncoder.encode("Teste123"));
                    user.setRole(Role.BUYER);
                    return userRepository.save(user);
                  });

      log.info("Test user created/found: {}", testUser.getEmail());

      // Verificar se já existem veículos
      if (vehicleRepository.count() == 0) {
        seedVehicles(testUser);
        log.info("Vehicles seeded successfully");
      } else {
        log.info("Vehicles already exist, skipping vehicle seeding");
      }

      // Verificar se já existem peças
      if (partRepository.count() == 0) {
        seedParts(testUser);
        log.info("Parts seeded successfully");
      } else {
        log.info("Parts already exist, skipping parts seeding");
      }

      log.info("Data seeding completed!");
    };
  }

  private void seedVehicles(User owner) {
    List<Vehicle> vehicles =
        Arrays.asList(
            // Carros
            createVehicle(
                owner,
                VehicleType.CAR,
                VehicleStatus.FOR_SALE,
                "Toyota",
                "Corolla",
                2020,
                "Branco",
                45000,
                "CARRO SEDAN - Excelente estado, único dono, histórico completo de manutenção. Veículo familiar ideal para uso diário, económico e confiável. Documentação em dia, sem acidentes.",
                "Gasolina",
                "Automática",
                1.8,
                140,
                4,
                5,
                1,
                false),
            createVehicle(
                owner,
                VehicleType.CAR,
                VehicleStatus.FOR_SALE,
                "Honda",
                "Civic",
                2019,
                "Preto",
                42000,
                "CARRO SEDAN - Bem conservado, revisões em dia, sem acidentes. Veículo desportivo com excelente performance, ideal para quem busca economia e potência.",
                "Gasolina",
                "Manual",
                1.6,
                130,
                4,
                5,
                1,
                false),
            createVehicle(
                owner,
                VehicleType.CAR,
                VehicleStatus.FOR_SALE,
                "BMW",
                "320i",
                2021,
                "Azul",
                85000,
                "CARRO SEDAN PREMIUM - Veículo premium alemão, todos os extras, garantia de fábrica. Conforto máximo, tecnologia de ponta, ideal para executivos.",
                "Gasolina",
                "Automática",
                2.0,
                184,
                4,
                5,
                0,
                false),
            createVehicle(
                owner,
                VehicleType.CAR,
                VehicleStatus.FOR_SALE,
                "Mercedes-Benz",
                "C200",
                2020,
                "Prata",
                92000,
                "CARRO SEDAN DE LUXO - Luxo e conforto alemão, equipamento completo. Veículo premium com acabamento de alta qualidade, ideal para quem busca elegância.",
                "Gasolina",
                "Automática",
                1.5,
                184,
                4,
                5,
                1,
                false),
            createVehicle(
                owner,
                VehicleType.CAR,
                VehicleStatus.FOR_SALE,
                "Volkswagen",
                "Golf",
                2018,
                "Vermelho",
                38000,
                "CARRO HATCHBACK - Económico e confiável alemão, ideal para cidade. Perfeito para uso diário, baixo consumo, manutenção acessível.",
                "Gasolina",
                "Manual",
                1.4,
                150,
                5,
                5,
                2,
                false),
            // Motos
            createVehicle(
                owner,
                VehicleType.MOTORCYCLE,
                VehicleStatus.FOR_SALE,
                "Honda",
                "CB 600F",
                2019,
                "Vermelho",
                18000,
                "MOTO DESPORTIVA - Moto desportiva, bem mantida, ideal para estrada. Excelente para viagens longas e uso urbano. Manutenção em dia, pneus novos.",
                "Gasolina",
                "Manual",
                0.6,
                95,
                null,
                2,
                1,
                false),
            createVehicle(
                owner,
                VehicleType.MOTORCYCLE,
                VehicleStatus.FOR_SALE,
                "Yamaha",
                "MT-07",
                2020,
                "Preto",
                22000,
                "MOTO NAKED - Naked bike potente japonesa, excelente para cidade e estrada. Design moderno, performance excecional, ideal para pilotos experientes.",
                "Gasolina",
                "Manual",
                0.7,
                74,
                null,
                2,
                0,
                false),
            createVehicle(
                owner,
                VehicleType.MOTORCYCLE,
                VehicleStatus.FOR_SALE,
                "Kawasaki",
                "Ninja 650",
                2021,
                "Verde",
                25000,
                "MOTO SPORT - Sport bike japonesa, design agressivo, performance excecional. Ideal para pista e estrada, velocidade máxima, tecnologia avançada.",
                "Gasolina",
                "Manual",
                0.65,
                68,
                null,
                2,
                0,
                false),
            createVehicle(
                owner,
                VehicleType.MOTORCYCLE,
                VehicleStatus.FOR_SALE,
                "Suzuki",
                "GSX-R750",
                2018,
                "Azul",
                28000,
                "MOTO SUPERESPORTIVA - Superesportiva japonesa, velocidade e precisão máximas. Para pilotos experientes que buscam performance extrema em pista.",
                "Gasolina",
                "Manual",
                0.75,
                150,
                null,
                2,
                1,
                false),
            createVehicle(
                owner,
                VehicleType.MOTORCYCLE,
                VehicleStatus.FOR_SALE,
                "Ducati",
                "Monster 696",
                2019,
                "Laranja",
                32000,
                "MOTO ITALIANA - Moto italiana icónica, design único e elegante. Combinação perfeita de estilo e performance, peça de colecionador.",
                "Gasolina",
                "Manual",
                0.7,
                80,
                null,
                2,
                1,
                false),
            // SUVs
            createVehicle(
                owner,
                VehicleType.SUV,
                VehicleStatus.FOR_SALE,
                "Toyota",
                "RAV4",
                2021,
                "Branco",
                65000,
                "CARRO SUV - SUV familiar japonês, espaço e conforto, tração 4x4. Ideal para família, viagens longas e estradas difíceis. 7 lugares.",
                "Gasolina",
                "Automática",
                2.5,
                203,
                5,
                5,
                0,
                false),
            createVehicle(
                owner,
                VehicleType.SUV,
                VehicleStatus.FOR_SALE,
                "Nissan",
                "X-Trail",
                2020,
                "Prata",
                58000,
                "CARRO SUV - SUV robusto japonês, ideal para família grande. Espaçoso, confortável, seguro e económico. Perfeito para uso familiar.",
                "Gasolina",
                "Automática",
                2.0,
                144,
                5,
                7,
                1,
                false),
            // Pickups
            createVehicle(
                owner,
                VehicleType.PICKUP,
                VehicleStatus.FOR_SALE,
                "Ford",
                "Ranger",
                2020,
                "Branco",
                72000,
                "CARRO PICKUP - Pickup robusta americana, ideal para trabalho e lazer. Resistente, potente, capacidade de carga elevada. Perfeita para todo-terreno.",
                "Diesel",
                "Manual",
                2.2,
                160,
                4,
                5,
                1,
                false),
            createVehicle(
                owner,
                VehicleType.PICKUP,
                VehicleStatus.FOR_SALE,
                "Toyota",
                "Hilux",
                2019,
                "Prata",
                68000,
                "CARRO PICKUP - Pickup confiável japonesa, resistente e durável. Lenda da confiabilidade, ideal para trabalho pesado e aventuras off-road.",
                "Diesel",
                "Manual",
                2.4,
                150,
                4,
                5,
                2,
                false));

    vehicleRepository.saveAll(vehicles);
  }

  private Vehicle createVehicle(
      User owner,
      VehicleType type,
      VehicleStatus status,
      String brand,
      String model,
      Integer year,
      String color,
      Integer price,
      String description,
      String fuelType,
      String transmissionType,
      Double engineSize,
      Integer horsePower,
      Integer numberOfDoors,
      Integer numberOfSeats,
      Integer previousOwners,
      Boolean accidentHistory) {
    Vehicle vehicle = new Vehicle();
    vehicle.setOwner(owner);
    vehicle.setType(type);
    vehicle.setStatus(status);
    vehicle.setBrand(brand);
    vehicle.setModel(model);
    vehicle.setYear(year);
    vehicle.setColor(color);
    vehicle.setMileageKm((int) (Math.random() * 100000) + 10000);
    vehicle.setPrice(BigDecimal.valueOf(price));
    vehicle.setCurrency("AOA");
    vehicle.setDescription(description);
    vehicle.setFuelType(fuelType);
    vehicle.setTransmissionType(transmissionType);
    vehicle.setEngineSize(engineSize);
    vehicle.setHorsePower(horsePower);
    vehicle.setNumberOfDoors(numberOfDoors);
    vehicle.setNumberOfSeats(numberOfSeats);
    vehicle.setPreviousOwners(previousOwners);
    vehicle.setAccidentHistory(accidentHistory);
    return vehicle;
  }

  private void seedParts(User seller) {
    List<Part> parts =
        Arrays.asList(
            // Peças de Motor
            createPart(
                seller,
                PartCategory.ENGINE,
                PartStatus.AVAILABLE,
                "Filtro de Ar Original",
                "PEÇA DE MOTOR - Filtro de ar original para Toyota Corolla 2015-2020. Peça genuína com garantia de fábrica. Melhora performance e economia de combustível.",
                15000,
                "FT-1234",
                "Toyota",
                "Compatível com Toyota Corolla, Camry",
                "NOVO",
                10,
                12),
            createPart(
                seller,
                PartCategory.ENGINE,
                PartStatus.AVAILABLE,
                "Óleo Motor 5W-30",
                "PEÇA DE MOTOR - Óleo sintético de alta qualidade, 5 litros. Proteção superior do motor, reduz desgaste, aumenta vida útil. Recomendado para todos os veículos.",
                25000,
                "OIL-5W30",
                "Castrol",
                "Universal",
                "NOVO",
                50,
                6),
            createPart(
                seller,
                PartCategory.ENGINE,
                PartStatus.AVAILABLE,
                "Correia de Distribuição",
                "PEÇA DE MOTOR - Kit completo correia de distribuição Honda Civic. Peça essencial para manutenção preventiva, evita danos graves no motor.",
                45000,
                "BELT-HC-2019",
                "Gates",
                "Honda Civic 2016-2021",
                "NOVO",
                5,
                24),
            // Peças de Transmissão
            createPart(
                seller,
                PartCategory.TRANSMISSION,
                PartStatus.AVAILABLE,
                "Óleo de Transmissão ATF",
                "PEÇA DE TRANSMISSÃO - Óleo para transmissão automática, 4 litros. Mantém transmissão suave, prolonga vida útil, essencial para manutenção.",
                35000,
                "ATF-4L",
                "Valvoline",
                "Universal para transmissões automáticas",
                "NOVO",
                20,
                12),
            createPart(
                seller,
                PartCategory.TRANSMISSION,
                PartStatus.AVAILABLE,
                "Embreagem Completa",
                "PEÇA DE TRANSMISSÃO - Kit embreagem completo para veículos médios. Substituição completa, alta qualidade, fácil instalação.",
                85000,
                "CLUTCH-KIT-M",
                "Luk",
                "Compatível com vários modelos",
                "NOVO",
                8,
                12),
            // Peças Elétricas
            createPart(
                seller,
                PartCategory.ELECTRICAL,
                PartStatus.AVAILABLE,
                "Bateria 12V 60Ah",
                "PEÇA ELÉTRICA - Bateria automotiva de alta performance. Início rápido, longa duração, ideal para todos os veículos. Garantia de fábrica.",
                55000,
                "BAT-60AH",
                "Varta",
                "Universal 12V",
                "NOVO",
                15,
                24),
            createPart(
                seller,
                PartCategory.ELECTRICAL,
                PartStatus.AVAILABLE,
                "Alternador Recondicionado",
                "PEÇA ELÉTRICA - Alternador recondicionado para Toyota. Testado e garantido, funciona como novo, preço acessível.",
                120000,
                "ALT-TOY-REC",
                "Toyota",
                "Toyota Corolla, Camry",
                "RECONDICIONADO",
                3,
                6),
            // Peças de Carroceria
            createPart(
                seller,
                PartCategory.BODY,
                PartStatus.AVAILABLE,
                "Para-choques Dianteiro",
                "PEÇA DE CARROCERIA - Para-choques dianteiro original Honda Civic. Peça genuína, acabamento perfeito, instalação profissional disponível.",
                95000,
                "BUMPER-HC-F",
                "Honda",
                "Honda Civic 2016-2021",
                "NOVO",
                2,
                12),
            createPart(
                seller,
                PartCategory.BODY,
                PartStatus.AVAILABLE,
                "Farol Dianteiro LED",
                "PEÇA DE CARROCERIA - Farol dianteiro LED completo, lado esquerdo. Iluminação superior, design moderno, fácil instalação.",
                180000,
                "HEADLIGHT-LED-L",
                "Osram",
                "Vários modelos compatíveis",
                "NOVO",
                4,
                24),
            // Peças de Suspensão
            createPart(
                seller,
                PartCategory.SUSPENSION,
                PartStatus.AVAILABLE,
                "Amortecedor Dianteiro",
                "PEÇA DE SUSPENSÃO - Amortecedor dianteiro original, par. Conforto e segurança, qualidade premium, garantia de fábrica.",
                150000,
                "SHOCK-F-PAIR",
                "Monroe",
                "Compatível com vários modelos",
                "NOVO",
                6,
                12),
            createPart(
                seller,
                PartCategory.SUSPENSION,
                PartStatus.AVAILABLE,
                "Mola Suspensão Traseira",
                "PEÇA DE SUSPENSÃO - Mola de suspensão traseira, par. Restaura altura original, melhora estabilidade, durabilidade comprovada.",
                85000,
                "SPRING-R-PAIR",
                "KYB",
                "Universal",
                "NOVO",
                8,
                12),
            // Peças de Travagem
            createPart(
                seller,
                PartCategory.BRAKES,
                PartStatus.AVAILABLE,
                "Pastilhas de Travão Dianteiras",
                "PEÇA DE TRAVAGEM - Kit pastilhas de travão dianteiras, alta qualidade. Segurança máxima, frenagem eficiente, fácil instalação.",
                45000,
                "BRAKE-PAD-F",
                "Brembo",
                "Compatível com vários modelos",
                "NOVO",
                12,
                12),
            createPart(
                seller,
                PartCategory.BRAKES,
                PartStatus.AVAILABLE,
                "Disco de Travão",
                "PEÇA DE TRAVAGEM - Disco de travão dianteiro, par. Alta resistência ao desgaste, performance superior, segurança garantida.",
                95000,
                "BRAKE-DISC-F",
                "Brembo",
                "Vários modelos",
                "NOVO",
                6,
                12),
            // Pneus e Rodas
            createPart(
                seller,
                PartCategory.TIRES_WHEELS,
                PartStatus.AVAILABLE,
                "Pneu 205/55 R16",
                "PEÇA DE PNEU - Pneu radial de alta qualidade, unidade. Aderência superior, baixo ruído, longa duração. Ideal para uso misto.",
                65000,
                "TIRE-205-55-16",
                "Michelin",
                "Universal 205/55 R16",
                "NOVO",
                20,
                36),
            createPart(
                seller,
                PartCategory.TIRES_WHEELS,
                PartStatus.AVAILABLE,
                "Jante Liga Leve 16\"",
                "PEÇA DE RODA - Jante de liga leve, 4 unidades. Design esportivo, leve e resistente, melhora performance e estética do veículo.",
                250000,
                "WHEEL-16-ALLOY",
                "OZ Racing",
                "Universal 16 polegadas",
                "NOVO",
                3,
                24),
            // Acessórios
            createPart(
                seller,
                PartCategory.ACCESSORIES,
                PartStatus.AVAILABLE,
                "Tapetes Automotivos",
                "PEÇA ACESSÓRIO - Kit completo tapetes para carro médio. Proteção do estofamento, fácil limpeza, encaixe perfeito.",
                25000,
                "MATS-SET",
                "WeatherTech",
                "Universal",
                "NOVO",
                15,
                12),
            createPart(
                seller,
                PartCategory.ACCESSORIES,
                PartStatus.AVAILABLE,
                "Capa de Volante",
                "PEÇA ACESSÓRIO - Capa de volante em couro, universal. Conforto e estilo, fácil instalação, protege volante original.",
                15000,
                "STEERING-COVER",
                "Generic",
                "Universal",
                "NOVO",
                25,
                6));

    partRepository.saveAll(parts);
  }

  private Part createPart(
      User seller,
      PartCategory category,
      PartStatus status,
      String name,
      String description,
      Integer price,
      String partNumber,
      String brand,
      String compatibleVehicles,
      String conditionType,
      Integer quantityAvailable,
      Integer warrantyMonths) {
    Part part = new Part();
    part.setSeller(seller);
    part.setCategory(category);
    part.setStatus(status);
    part.setName(name);
    part.setDescription(description);
    part.setPrice(BigDecimal.valueOf(price));
    part.setCurrency("AOA");
    part.setPartNumber(partNumber);
    part.setBrand(brand);
    part.setCompatibleVehicles(compatibleVehicles);
    part.setConditionType(conditionType);
    part.setQuantityAvailable(quantityAvailable);
    part.setWarrantyMonths(warrantyMonths);
    return part;
  }
}
