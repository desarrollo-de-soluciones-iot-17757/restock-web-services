workspace "Restock Platform" "Inventory management system with IoT integration" {

    model {

    !impliedRelationships false

        // =====================================================================
        // PEOPLE
        // =====================================================================

        visitor = person "Visitor" "Anonymous user who browses public website content." "Visitor"

        restaurantAdmin = person "Restaurant Administrator" "User who manages restaurant inventory, recipes, sales, stock and branch operations." "TargetSegments"

        retailAdmin = person "Retail Administrator" "User who manages retail inventory, products, stock and branch operations." "TargetSegments"


        // =====================================================================
        // RESTOCK SOFTWARE SYSTEM
        // =====================================================================

        restock = softwareSystem "Restock Platform" "Inventory management platform with web, mobile and IoT support." "Platform" {

            // -----------------------------------------------------------------
            // CLIENT APPLICATIONS
            // -----------------------------------------------------------------

            landingPage = container "Landing Page" "Public static website that presents Restock, its benefits, plans and call-to-action links." "HTML5, CSS3, JavaScript" "LandingPage" {
                landingWebUI = component "Landing Web UI" "Shows product information, benefits, target users, plans, testimonials and call-to-action buttons." "HTML5, CSS3, JavaScript"
            }

            staticApp = container "Web Server" "Static file that delivers Angular PWA content to the user's browser." "TypeScript, Angular" "StaticApp"

            webApplication = container "Web Application" "Client-side web application executed in the user's browser to manage inventory, devices, tracking, alerts and reports." "TypeScript, Angular" "Frontend" {
                webIam = component "IAM UI" "Manages sign-in and sign-up screens." "TypeScript, Angular"
                webProfile = component "Profiles UI" "Manages user and business profile views." "TypeScript, Angular"
                webSubscriptions = component "Subscriptions UI" "Allows users to view, choose, renew or cancel subscription plans." "TypeScript, Angular"
                webAssetAndResource = component "ARM UI" "Manages branches, supplies, custom supplies, batches and inventory." "TypeScript, Angular"
                webDeviceManagement = component "Devices UI" "Allows users to register, assign, configure and deactivate IoT devices." "TypeScript, Angular"
                webTracking = component "Tracking UI" "Displays plate state, device health, stock comparison and reconciliation information." "TypeScript, Angular"
                webCommunications = component "Communications UI" "Displays alerts and notification history." "TypeScript, Angular"
                webAnalytics = component "Analytics UI" "Displays dashboard indicators, recent alerts, low stock supplies and recent registered supplies." "TypeScript, Angular"
                webServiceDesign = component "Planning UI" "Manages recipes for restaurants and kits for retail stores." "TypeScript, Angular"
                webSalesManagement = component "Sales UI" "Manages sales order registration and visualization." "TypeScript, Angular"
            }

            mobileApplication = container "Restock Mobile Application" "Cross-platform mobile application that allows restaurant and retail managers to manage inventory and see devices, tracking and alerts data." "Dart, Flutter" "MobileApplication" {
                mobileIam = component "IAM UI" "Manages sign-in and sign-up operations." "Dart, Flutter"
                mobileProfile = component "Profiles UI" "Manages user's personal profile and business profile." "Dart, Flutter"
                mobileSubscriptions = component "Subscriptions UI" "Handles subscription benefits viewing, choosing and renewing." "Dart, Flutter"
                mobileAssetAndResource = component "ARM UI" "Manages branches, supplies, custom supplies, batches and inventory." "Dart, Flutter"
                mobileDeviceManagement = component "Devices UI" "Allows users view its IoT registered devices." "Dart, Flutter"
                mobileTracking = component "Tracking UI" "Displays device telemetry, health, stock comparison and reconciliation data." "Dart, Flutter"
                mobileCommunications = component "Communications UI" "Displays alerts and notification history." "Dart, Flutter"
                mobileAnalytics = component "Analytics UI" "Displays dashboard indicators and stock summaries." "Dart, Flutter"
            }


            // -----------------------------------------------------------------
            // API GATEWAY AND LOAD BALANCING
            // -----------------------------------------------------------------

            apiGateway = container "Kong Gateway" "API Gateway and Auth Proxy that validates tokens, applies rate limiting and routes authorized requests to internal bounded contexts." "Kong Gateway" "ApiGateway"

            nginxLoadBalancer = container "NGINX Load Balancer" "Redirector and load balancer that forwards user requests to a healthy backend gateway instance when one instance becomes unavailable." "NGINX" "LoadBalancer"


            // -----------------------------------------------------------------
            // IOT COMMUNICATION
            // -----------------------------------------------------------------

            mqttBroker = container "MQTT Broker" "Message broker that receives telemetry and device events from the edge application and delivers them to backend bounded contexts." "MQTT Broker, HiveMQ / Mosquitto / EMQX" "MQTTBroker"

            edgeApplication = container "Restock Edge Application" "Local edge software installed in the branch. Receives sensor readings from the embedded application, performs local validation and publishes telemetry to the MQTT broker." "Python, Flask" "Edge" {
                edgeTelemetryReceiver = component "Telemetry Receiver" "Receives weight, temperature, humidity and health readings from the embedded application." "Python" "Component"
                edgeDataProcessor = component "Edge Data Processor" "Filters noisy readings, detects stable values and prepares telemetry messages." "Python" "Component"
                edgeDeviceIdentityManager = component "Device Identity Manager" "Authenticates the embedded device before accepting telemetry." "Python" "Component"
                edgeConfigurationCache = component "Configuration Cache" "Stores current device configuration, assigned custom supply, thresholds and powering schedule." "Python" "Component"
                edgeCommandHandler = component "Command Handler" "Receives device configuration from the broker and forwards them to the edge application." "Python" "Component"
                edgeDeviceCommunication = component "Device Communication" "Receives processed data and sends to the device for showing." "Python" "Component"
                edgeMqttClient = component "MQTT Client" "Publishes telemetry and device events to the MQTT broker and subscribes to configuration command topics." "Python" "Component"
                edgeLocalStorage = component "Local Storage Adapter" "Stores pending telemetry and configuration data when the connection is unavailable." "Python, SQLite Adapter" "Component"
            }

            embeddedApplication = container "Restock Embedded Application" "Firmware that controls the smart scale sensors and sends readings to the edge application." "C++ / ESP32" "Embedded" {
                embeddedDeviceController = component "Device Controller" "Controls the physical sensors and reads raw hardware values." "C++"
                embeddedWeightSensorAdapter = component "Weight Sensor Adapter" "Reads weight data from the load cells through the HX711 converter." "C++"
                embeddedEnvironmentSensorAdapter = component "Temperature and Humidity Sensor Adapter" "Reads temperature and humidity values from the DHT22 sensor." "C++"
                embeddedLcdDisplayController = component "LCD Display Controller" "Shows weight, temperature, humidity and device state on the local LCD screen." "C++"
                embeddedHealthMonitor = component "Health Monitor" "Checks sensor status, connectivity state and basic device health information." "C++"
                embeddedCommunicationClient = component "Communication Client" "Sends sensor readings and health data to the edge application." "C++"
                embeddedLocalBuffer = component "Local Buffer" "Temporarily stores readings when communication with the edge application is unavailable." "C++"
            }


            // -----------------------------------------------------------------
            // CLOUD API BOUNDED CONTEXTS
            // -----------------------------------------------------------------

            cloudApi = container "Cloud REST API" "Manages and provides all functionalities to the client apps." "Java, Spring Boot" "Backend" {
                bcIAM = component "Identity and Access Management" "Manages authentication, authorization and user access control." "Java, Spring Boot" "Component"
                bcProfile = component "Profile and Preferences" "Manages personal profile, business profile and user preferences." "Java, Spring Boot" "Component"
                bcSubscriptions = component "Subscriptions and Payments" "Handles subscription lifecycle, plan selection, payment and renewal." "Java, Spring Boot" "Component"
                bcResource = component "Asset and Resource Management" "Manages business assets such as branches, supplies, custom supplies, batches and inventory. IoT device configuration is handled by Device Management." "Java, Spring Boot" "Component"
                bcDeviceManagement = component "Device Management" "Manages IoT device registration, credentials, branch assignment, custom supply assignment, threshold configuration, powering schedule and device lifecycle." "Java, Spring Boot" "Component"
                bcTracking = component "Tracking" "Processes telemetry, device health, plate state, stock comparison, thresholds and reconciliation tasks." "Java, Spring Boot" "Component"
                bcCommunications = component "Communications" "Handles alert and notification generation, persistence and delivery." "Java, Spring Boot" "Component"
                bcAnalytics = component "Analytics" "Builds dashboard information such as low stock supplies, zero stock supplies, last supplies registered and recent alerts." "Java, Spring Boot" "Component"
                bcPlanning = component "Service Design and Planning" "Handles recipe management for restaurants and kit management for retail stores." "Java, Spring Boot" "Component"
                bcSales = component "Sales Order Management" "Handles sales order registration and stock impact validation." "Java, Spring Boot" "Component"
            }


            // -----------------------------------------------------------------
            // DATABASES
            // -----------------------------------------------------------------

            mobileLocalDatabase = container "Mobile SQLite Database" "Stores cached application data locally on the mobile device." "SQLite" "LocalDatabase"

            mongoDatabase = container "MongoDB Database" "Stores user's personal, security and business data." "MongoDB" "DatabaseNoSql"

            redisDatabase = container "Redis Cache Server" "Caches frequently requested data like inventories, telemetry, dashboards, orders, kits and recipes" "Redis" "Cache"

            edgeLocalDatabase = container "Edge Local Database" "Stores recent readings, pending telemetry, device configuration and synchronization state for local resilience." "SQLite" "LocalDatabase"
        }


        // =====================================================================
        // EXTERNAL SYSTEMS
        // =====================================================================

        stripe = softwareSystem "Stripe" "External platform for payments and subscription billing." "ExternalSystem"
        cloudinary = softwareSystem "Cloudinary API" "External service for media storage and image management." "ExternalSystem"
        oneSignal = softwareSystem "OneSignal API" "External service for sending push notifications and alerts." "ExternalSystem"
        restockIotDevice = softwareSystem "Restock Smart Inventory Device Hardware" "Physical smart scale device composed of weight sensors, HX711, ESP32, DHT22 and LCD screen." "ExternalSystem"


        // =====================================================================
        // SYSTEM CONTEXT RELATIONSHIPS
        // =====================================================================

        visitor -> restaurantAdmin "Signs up via IAM to become a user in the platform
        visitor -> retailAdmin "Signs up via IAM to become a user in the platform"
        visitor -> restock "Browses public content (plans, information)" "HTTPS"

        restaurantAdmin -> restock "Uses to manage restaurant inventory, devices, stock and operations." "HTTPS"
        retailAdmin -> restock "Uses to manage retail inventory, devices, stock and operations." "HTTPS"

        restock -> stripe "Processes payments and subscriptions through." "HTTPS"
        restock -> cloudinary "Stores and retrieves media files through." "HTTPS"
        restock -> oneSignal "Sends notifications and alerts through." "HTTPS"
        restock -> restockIotDevice "Receives physical stock and environment data from." "WiFi / MQTT"


        // =====================================================================
        // CONTAINER RELATIONSHIPS
        // =====================================================================

        visitor -> landingPage "Browses public content (plans, information)" "HTTPS"

        restaurantAdmin -> staticApp "Uses to manage restaurant inventory, devices, stock and operations." "HTTPS"
        retailAdmin -> staticApp "Uses to manage retail inventory, devices, stock and operations." "HTTPS"

        restaurantAdmin -> mobileApplication "Uses to manage restaurant inventory and view devices, stock and operations data." "HTTPS"
        retailAdmin -> mobileApplication "Uses to manage restaurant inventory and view devices, stock and operations data." "HTTPS"

        landingPage -> staticApp "Redirects to the web client app" "HTTPS"
        landingPage -> mobileApplication "Redirects to the mobile app" "HTTPS"
        staticApp -> webApplication "Delivers PWA files" "HTTPS"

        webApplication -> nginxLoadBalancer "Sends HTTPS requests to" "JSON/HTTPS"
        webApplication -> stripe "Redirects users to external payment checkout when required" "HTTPS"
        mobileApplication -> nginxLoadBalancer "Sends HTTPS requests to" "JSON/HTTPS"
        nginxLoadBalancer -> apiGateway "Forwards requests to a healthy gateway instance" "HTTPS"

        mobileApplication -> mobileLocalDatabase "Caches and retrieves local data." "SQLite"

        apiGateway -> cloudApi "Routes requests to" "JSON/HTTPS"

        embeddedApplication -> edgeApplication "Sends raw sensor readings and device health data to." "WiFi / Local Network"
        edgeApplication -> mqttBroker "Publishes telemetry, health and device events to." "MQTT"
        mqttBroker -> apiGateway "Delivers IoT telemetry, health and device events to the backend entry point." "MQTT-to-HTTP Bridge / Webhook"

        cloudApi -> stripe "Processes payments and subscription billing with" "JSON/HTTPS"
        cloudApi -> cloudinary "Uploads and retrieves images with" "JSON/HTTPS"
        cloudApi -> oneSignal "Sends push notifications and alerts through" "JSON/HTTPS"
        cloudApi -> mqttBroker "Publishes device configuration commands, threshold changes, assignment changes and powering schedules to." "MQTT"
        cloudApi -> mongoDatabase "Stores data"
        cloudApi -> redisDatabase "Caches frequently used data"

        mqttBroker -> edgeApplication "Delivers device configuration commands to." "MQTT"
        edgeApplication -> embeddedApplication "Forwards configuration and power commands to." "WiFi / Local Network"

        edgeApplication -> edgeLocalDatabase "Stores and retrieves local telemetry, configuration and pending synchronization data." "SQLite"
        embeddedApplication -> restockIotDevice "Controls and reads sensors from." "GPIO / I2C / ADC"
        restockIotDevice -> embeddedApplication "Sends weight, temperature and humidity readings to." "Hardware signals"


        // =====================================================================
        // COMPONENT RELATIONSHIPS - LANDING PAGE
        // =====================================================================

        visitor -> landingWebUI "Browses public content (plans, information)" "HTTPS"
        landingWebUI -> staticApp "Redirects to the web client app" "HTTPS"
        landingWebUI -> mobileApplication "Redirects to the mobile app" "HTTPS"


        // =====================================================================
        // COMPONENT RELATIONSHIPS - WEB CLIENT
        // =====================================================================

        webIam -> nginxLoadBalancer "Sends authentication requests." "JSON/HTTPS"
        webProfile -> nginxLoadBalancer "Requests and updates profile information." "JSON/HTTPS"
        webSubscriptions -> nginxLoadBalancer "Requests subscription information and starts payment flows." "JSON/HTTPS"
        webAssetAndResource -> nginxLoadBalancer "Manages branches, supplies, batches and inventory." "JSON/HTTPS"
        webDeviceManagement -> nginxLoadBalancer "Manages device registration, assignment, thresholds and schedule." "JSON/HTTPS"
        webTracking -> nginxLoadBalancer "Requests plate state, device health, stock comparison and reconciliation information." "JSON/HTTPS"
        webCommunications -> nginxLoadBalancer "Requests alerts and notification history." "JSON/HTTPS"
        webAnalytics -> nginxLoadBalancer "Requests dashboard data." "JSON/HTTPS"
        webServiceDesign -> nginxLoadBalancer "Manages recipes and kits." "JSON/HTTPS"
        webSalesManagement -> nginxLoadBalancer "Registers and retrieves sales orders." "JSON/HTTPS"
        webSubscriptions -> stripe "Redirects users to external payment checkout when required." "HTTPS"


        // =====================================================================
        // COMPONENT RELATIONSHIPS - MOBILE CLIENT
        // =====================================================================

        mobileIam -> nginxLoadBalancer "Sends authentication requests." "JSON/HTTPS"
        mobileProfile -> nginxLoadBalancer "Requests and updates profile information." "JSON/HTTPS"
        mobileSubscriptions -> nginxLoadBalancer "Requests subscription information and starts payment flows." "JSON/HTTPS"
        mobileAssetAndResource -> nginxLoadBalancer "Manages branches, supplies, batches and inventory." "JSON/HTTPS"
        mobileDeviceManagement -> nginxLoadBalancer "Manages device registration, assignment, thresholds and schedule." "JSON/HTTPS"
        mobileTracking -> nginxLoadBalancer "Requests tracking and reconciliation data." "JSON/HTTPS"
        mobileCommunications -> nginxLoadBalancer "Requests alerts and notification history." "JSON/HTTPS"
        mobileAnalytics -> nginxLoadBalancer "Requests dashboard data." "JSON/HTTPS"

        mobileAssetAndResource -> mobileLocalDatabase "Caches asset and inventory data."
        mobileTracking -> mobileLocalDatabase "Caches last tracking data."


        // =====================================================================
        // COMPONENT RELATIONSHIPS - CLOUD API
        // =====================================================================

        bcIAM -> mongoDatabase "Reads and writes users, roles and credentials." "Spring Data MongoDB"
        bcProfile -> mongoDatabase "Reads and writes profiles and preferences." "Spring Data MongoDB"
        bcSubscriptions -> mongoDatabase "Reads and writes accounts, plans and subscriptions." "Spring Data MongoDB"
        bcResource -> mongoDatabase "Reads and writes branches, supplies, batches and inventory records." "Spring Data MongoDB"
        bcDeviceManagement -> mongoDatabase "Reads and writes devices, credentials and device configurations." "Spring Data MongoDB"
        bcTracking -> mongoDatabase "Reads and writes plate states, health records, comparisons, thresholds and reconciliation tasks." "Spring Data MongoDB"
        bcCommunications -> mongoDatabase "Reads and writes notifications and alerts." "Spring Data MongoDB"
        bcPlanning -> mongoDatabase "Reads and writes recipes and kits." "Spring Data MongoDB"
        bcSales -> mongoDatabase "Reads and writes sales orders." "Spring Data MongoDB"
        bcDeviceManagement -> mqttBroker "Publishes device configuration commands, threshold changes, assignment changes and powering schedules to." "MQTT"

        bcResource -> redisDatabase "Caches frequently requested inventory, branch, supply and stock data." "Redis Protocol"
        bcTracking -> redisDatabase "Caches latest telemetry, plate state, device health and stock comparison data." "Redis Protocol"
        bcAnalytics -> redisDatabase "Caches dashboard summaries, low stock indicators and recent operational metrics." "Redis Protocol"

        bcSubscriptions -> stripe "Processes payments and subscription billing with." "JSON/HTTPS"
        bcResource -> cloudinary "Uploads and retrieves branch and custom supply images with." "JSON/HTTPS"
        bcProfile -> cloudinary "Uploads and retrieves profile or business images with." "JSON/HTTPS"
        bcPlanning -> cloudinary "Uploads and retrieves profile or business images with." "JSON/HTTPS"
        bcCommunications -> oneSignal "Sends push notifications and alerts through." "JSON/HTTPS"

        // BOUNDED CONTEXT RELATIONSHIPS

        bcProfile -> bcIAM "Checks if request is authorized" "Internal"

        bcSubscriptions -> bcIAM "Checks if request is authorized" "Internal"

        bcResource -> bcIAM "Checks if request is authorized" "Internal"
        bcResource -> bcSubscriptions "Checks user's current resource usage" "ACL"
        bcResource -> bcCommunications "Alerts low and zero stock critical events" "ACL"

        bcAnalytics -> bcIAM "Checks if request is authorized" "Internal"
        bcAnalytics -> bcTracking "Uses for building devices analytical data"
        bcAnalytics -> bcResource "Uses for building inventory analytical data"
        bcAnalytics -> bcCommunications "Uses for building latest alerts analytical data"

        bcDeviceManagement -> bcIAM "Checks if request is authorized" "Internal"
        bcDeviceManagement -> bcResource "Checks assigned supply thresholds and average weight" "Internal"

        bcTracking -> bcIAM "Checks if request is authorized" "Internal"
        bcTracking -> bcResource "Compares physical and digital stock for the same supply" "ACL"
        bcTracking -> bcCommunications "Alerts a stock discrepancy" "ACL"

        bcCommunications -> bcIAM "Checks if request is authorized" "Internal"

        bcPlanning -> bcIAM "Checks if request is authorized" "Internal"
        bcPlanning -> bcSubscriptions "Checks user's current resource usage" "ACL"
        bcPlanning -> bcResource "Subtracks supply stock asociated to recipe/kit" "ACL"

        bcSales -> bcIAM "Checks if request is authorized" "Internal"
        bcSales -> bcResource "Subtracks sold supply stock" "ACL"
        bcSales -> bcPlanning "Registers recipe/kit sale" "ACL"
        bcSales -> bcCommunications "Notifies a completed sale" "ACL"


        // =====================================================================
        // COMPONENT RELATIONSHIPS - EDGE APPLICATION
        // =====================================================================

        embeddedApplication -> edgeTelemetryReceiver "Sends raw sensor readings and health data to" "WiFi / Local Network"
        edgeTelemetryReceiver -> edgeDeviceIdentityManager "Requests device authentication from"
        edgeTelemetryReceiver -> edgeDataProcessor "Sends authorized telemetry to"
        edgeDataProcessor -> edgeConfigurationCache "Checks assigned supply, thresholds and schedule using."
        edgeDataProcessor -> edgeDeviceCommunication "Sends processed data"
        edgeDataProcessor -> edgeMqttClient "Publishes processed telemetry through"
        edgeMqttClient -> mqttBroker "Publishes telemetry and health messages to" "MQTT"
        mqttBroker -> edgeMqttClient "Delivers configuration commands from Device Management to." "MQTT"
        edgeMqttClient -> edgeCommandHandler "Forwards received configuration commands to"
        edgeCommandHandler -> edgeConfigurationCache "Sends supply assignment and threshold commands to"
        edgeConfigurationCache -> edgeLocalDatabase "Stores device configuration in." "SQLite"
        edgeLocalStorage -> edgeLocalDatabase "Stores pending telemetry and synchronization state in." "SQLite"
        edgeDataProcessor -> edgeLocalStorage "Stores pending telemetry when broker connection is unavailable."
        edgeDeviceCommunication -> embeddedApplication "Sends processed data to" "WiFi / Local Network"


        // =====================================================================
        // COMPONENT RELATIONSHIPS - EMBEDDED APPLICATION
        // =====================================================================

        edgeApplication -> embeddedCommunicationClient "Sends processed data for showing in the display" "WiFi / Local Network"

        embeddedDeviceController -> embeddedWeightSensorAdapter "Requests weight readings from."
        embeddedDeviceController -> embeddedEnvironmentSensorAdapter "Requests temperature and humidity readings from."
        embeddedWeightSensorAdapter -> restockIotDevice "Reads load cell values through HX711 from." "Hardware"
        embeddedEnvironmentSensorAdapter -> restockIotDevice "Reads DHT22 values from." "Hardware"
        embeddedDeviceController -> embeddedLcdDisplayController "Sends display values to."
        embeddedDeviceController -> embeddedHealthMonitor "Requests sensor and connectivity health status from."
        embeddedDeviceController -> embeddedCommunicationClient "Sends collected readings to."
        embeddedCommunicationClient -> edgeApplication "Sends telemetry and health data to." "WiFi / Local Network"
        embeddedCommunicationClient -> embeddedLocalBuffer "Stores readings when communication is unavailable."
        embeddedLocalBuffer -> embeddedCommunicationClient "Provides pending readings when communication is restored."


        // =====================================================================
        // DEPLOYMENT ENVIRONMENT - PRODUCTION
        // =====================================================================

        deploymentEnvironment "Production" {
            deploymentNode "Vercel" "Cloud platform for hosting and delivering the public website and web application." "Vercel Edge Network" {
                tags "HostingNode"

                deploymentNode "Landing Page Hosting" "Public static website delivered to visitors." "Vercel Edge Network" {
                    containerInstance landingPage
                }

                deploymentNode "Web Application Hosting" "Static Angular files served to the user's browser." "Vercel Edge Network" {
                    containerInstance staticApp
                }
            }

            deploymentNode "User Laptop / Desktop" "End-user computer used to access Restock through a browser." "Windows 11 / macOS / Linux" {
                tags "ClientNode"

                deploymentNode "Web Browser" "Browser installed on the end-user device." "Chrome / Safari / Edge / Firefox" {
                    containerInstance webApplication
                }
            }

            deploymentNode "Mobile Device" "End-user smartphone or tablet used to access the mobile app." "iOS / Android" {
                tags "ClientNode"

                containerInstance mobileApplication
                containerInstance mobileLocalDatabase
            }

            deploymentNode "Render" "Cloud platform where backend services are deployed and executed." "Render Web Service / Docker" {
                tags "BackendHost"

                containerInstance apiGateway
                containerInstance cloudApi
            }

            deploymentNode "MQTT Broker Cloud" "Managed or self-hosted MQTT broker used for IoT telemetry and device commands." "HiveMQ Cloud / Mosquitto / EMQX" {
                tags "BrokerHost"

                containerInstance mqttBroker
            }

            deploymentNode "MongoDB Atlas" "Managed cloud database service used by the backend bounded contexts." "MongoDB Atlas Cluster" {
                tags "DatabaseHost"

                containerInstance mongoDatabase
            }

            deploymentNode "Redis Cloud" "Managed Redis service used for cache, rate limiting and temporary data." "Redis Cloud / Upstash / Render Redis" {
                tags "CacheHost"

                containerInstance redisDatabase
            }

            deploymentNode "Edge Device / Raspberry Pi" "Local edge computing device installed in the business branch." "Raspberry Pi OS / Linux" {
                tags "EdgeHost"

                containerInstance edgeApplication
                containerInstance edgeLocalDatabase
            }

            deploymentNode "Smart Scale Hardware" "IoT smart scale device that captures weight, temperature and humidity readings." "ESP32 / Sensors / LCD" {
                tags "SmartScaleHost"

                containerInstance embeddedApplication
            }
        }
    }


    // =====================================================================
    // CONFIGURATION
    // =====================================================================

    configuration {
        scope softwaresystem
    }


    // =====================================================================
    // VIEWS
    // =====================================================================

    views {

        systemContext restock "SystemContext" {
            include *
            autolayout
        }

        container restock "ContainerView" {
            include *
            autolayout
        }

        component landingPage "LandingPageComponents" {
            include *
            autolayout
        }

        component webApplication "WebClientComponents" {
            include *
            autolayout
        }

        component mobileApplication "MobileClientComponents" {
            include *
            autolayout
        }

        component cloudApi "CloudRestAPIComponents" {
            include *
            autolayout
        }

        component edgeApplication "EdgeAppComponents" {
            include *
            autolayout
        }

        component embeddedApplication "EmbeddedAppComponents" {
            include *
            autolayout
        }

        deployment restock "Production" "ProductionDeployment" {
            include *
            autolayout
        }


        // =================================================================
        // STYLES
        // =================================================================

        styles {

            element "Element" {
                color "#ffffff"
                stroke "#2f2f2f"
                strokeWidth 2
                shape RoundedBox
            }

            element "Visitor" {
                shape Person
                background gray
                stroke gray
                color white
            }

            element "TargetSegments" {
                shape Person
                background "#1f4e79"
                stroke "#1f4e79"
                color "#ffffff"
            }

            element "Platform" {
                strokeWidth 7
                stroke "#f5a142"
                color "#f5a142"
            }

            element "Component" {
                shape Component
                strokeWidth 7
                stroke "#38bdf8"
                color "#38bdf8"
            }

            element "ExternalSystem" {
                shape Component
                background "#8c8c8c"
                stroke "#8c8c8c"
                color "#ffffff"
            }

            element "LandingPage" {
                shape WebBrowser
                strokeWidth 7
                stroke "#2b7bbb"
                color "#2b7bbb"
            }

            element "StaticApp" {
                shape RoundedBox
                strokeWidth 7
                stroke "#2b7bbb"
                color "#2b7bbb"
            }

            element "Frontend" {
                shape WebBrowser
                strokeWidth 7
                stroke "#2b7bbb"
                color "#2b7bbb"
            }

            element "MobileApplication" {
                shape MobileDeviceLandscape
                strokeWidth 7
                stroke "#2b7bbb"
                color "#2b7bbb"
            }

            element "ApiGateway" {
                shape RoundedBox
                strokeWidth 7
                stroke "#00b050"
                color "#00b050"
            }

            element "MQTTBroker" {
                shape Pipe
                strokeWidth 7
                stroke "#7030a0"
                color "#7030a0"
            }

            element "Backend" {
                shape RoundedBox
                strokeWidth 7
                stroke "#2b7bbb"
                color "#2b7bbb"
            }

            element "Edge" {
                shape Box
                strokeWidth 7
                stroke "#ff9900"
                color "#ff9900"
            }

            element "Embedded" {
                shape Box
                strokeWidth 7
                stroke "#ffcc00"
                color "#ffcc00"
            }

            element "LocalDatabase" {
                shape Cylinder
                strokeWidth 7
                stroke "#d7191c"
                color "#d7191c"
            }

            element "DatabaseNoSql" {
                shape Cylinder
                strokeWidth 7
                stroke "#d7191c"
                color "#d7191c"
            }

            element "Cache" {
                shape Cylinder
                strokeWidth 7
                stroke "#DC382D"
                color "#DC382D"
            }

            element "HostingNode" {
                background "#E0F2FE"
                stroke "#0284C7"
                color "#0F172A"
                strokeWidth 7
            }

            element "ClientNode" {
                background "#ECFEFF"
                stroke "#06B6D4"
                color "#0F172A"
                strokeWidth 7
            }

            element "BackendHost" {
                background "#DCFCE7"
                stroke "#22C55E"
                color "#0F172A"
                strokeWidth 7
            }

            element "BrokerHost" {
                background "#F3E8FF"
                stroke "#A855F7"
                color "#0F172A"
                strokeWidth 7
            }

            element "DatabaseHost" {
                background "#FEE2E2"
                stroke "#EF4444"
                color "#0F172A"
                strokeWidth 7
            }

            element "CacheHost" {
                background "#FFEDD5"
                stroke "#F97316"
                color "#0F172A"
                strokeWidth 7
            }

            element "EdgeHost" {
                background "#FEF3C7"
                stroke "#F59E0B"
                color "#0F172A"
                strokeWidth 7
            }

            element "SmartScaleHost" {
                background "#FEF9C3"
                stroke "#EAB308"
                color "#0F172A"
                strokeWidth 7
            }

            element "LoadBalancer" {
                shape RoundedBox
                strokeWidth 7
                stroke "#F97316"
                color "#F97316"
            }

            relationship "Relationship" {
                color "#bdbdbd"
                dashed true
                routing Direct
            }
        }
    }
}