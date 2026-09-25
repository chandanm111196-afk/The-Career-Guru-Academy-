package com.example.data.local

import com.example.data.model.*

object SeedData {
    val initialProfile = UserProfile(
        id = "current_user",
        name = "Sourav Mukherjee",
        mobile = "9093200422",
        email = "sourav.aspirant@gmail.com",
        role = UserRole.STUDENT,
        branch = "Suri, Birbhum",
        preferredExam = "WBCS",
        streakDays = 7,
        xpPoints = 1450,
        avatarRes = "avatar_1"
    )

    val liveClasses = listOf(
        LiveClass(
            id = "live_1",
            title = "West Bengal Geography & River Systems — Deep Dive",
            teacherName = "Prof. Anirban Sen",
            subject = "Geography",
            examCategory = "WBCS",
            timeLabel = "Today — 7:00 PM",
            isLiveNow = true,
            meetingUrl = "https://live.careerguru.in/wbcs-geo",
            durationMinutes = 75,
            date = "25 Sep 2026",
            viewersCount = 186
        ),
        LiveClass(
            id = "live_2",
            title = "SSC CGL 2026: Advanced Arithmetic & Tricky Algebra",
            teacherName = "Er. Subhajit Roy",
            subject = "Quantitative Aptitude",
            examCategory = "SSC",
            timeLabel = "Today — 8:30 PM",
            isLiveNow = false,
            meetingUrl = "https://live.careerguru.in/ssc-math",
            durationMinutes = 90,
            date = "25 Sep 2026",
            viewersCount = 112
        ),
        LiveClass(
            id = "live_3",
            title = "Daily Editorial & Current Affairs Discussion (WB Special)",
            teacherName = "Kalyan Dasgupta",
            subject = "Current Affairs",
            examCategory = "WBCS",
            timeLabel = "Tomorrow — 8:00 AM",
            isLiveNow = false,
            meetingUrl = "https://live.careerguru.in/daily-ca",
            durationMinutes = 60,
            date = "26 Sep 2026",
            viewersCount = 95
        ),
        LiveClass(
            id = "live_4",
            title = "RRB NTPC: General Science (Physics & Chemistry Fundamentals)",
            teacherName = "Dr. Mita Bannerjee",
            subject = "General Science",
            examCategory = "RRB",
            timeLabel = "Tomorrow — 6:00 PM",
            isLiveNow = false,
            meetingUrl = "https://live.careerguru.in/rrb-sci",
            durationMinutes = 60,
            date = "26 Sep 2026",
            viewersCount = 78
        )
    )

    val recordedClasses = listOf(
        RecordedClass(
            id = "rec_1",
            title = "Indian National Movement (1885-1947) Part 1",
            teacherName = "Prof. Anirban Sen",
            subject = "History",
            examCategory = "WBCS",
            chapter = "Modern History of India",
            duration = "52 mins",
            videoUrl = "https://vod.careerguru.in/history_inm_1.mp4",
            isBookmarked = true,
            viewsCount = 890,
            dateUploaded = "23 Sep 2026"
        ),
        RecordedClass(
            id = "rec_2",
            title = "Constitutional Framework: Preamble & Fundamental Rights",
            teacherName = "Adv. Souvik Ghosh",
            subject = "Polity",
            examCategory = "PSC",
            chapter = "Indian Constitution",
            duration = "45 mins",
            videoUrl = "https://vod.careerguru.in/polity_fr.mp4",
            isBookmarked = false,
            viewsCount = 1240,
            dateUploaded = "22 Sep 2026"
        ),
        RecordedClass(
            id = "rec_3",
            title = "Syllogism & Logical Deduction Masterclass",
            teacherName = "Priyanka Roy",
            subject = "Reasoning",
            examCategory = "BANKING",
            chapter = "Analytical Reasoning",
            duration = "60 mins",
            videoUrl = "https://vod.careerguru.in/reasoning_syl.mp4",
            isBookmarked = true,
            viewsCount = 670,
            dateUploaded = "21 Sep 2026"
        ),
        RecordedClass(
            id = "rec_4",
            title = "Child Development & Pedagogy (CDP) for Primary TET",
            teacherName = "Tanmoy Mondal",
            subject = "Pedagogy",
            examCategory = "TET",
            chapter = "Learning Theories & Piaget",
            duration = "50 mins",
            videoUrl = "https://vod.careerguru.in/tet_cdp.mp4",
            isBookmarked = false,
            viewsCount = 430,
            dateUploaded = "20 Sep 2026"
        )
    )

    val studyNotes = listOf(
        StudyNote(
            id = "note_1",
            title = "West Bengal Geography: Rivers, Districts & Mineral Resources",
            examCategory = "WBCS",
            subject = "Geography",
            description = "Complete hand-curated notes covering Bhagirathi-Hooghly, Damodar, Teesta basins, district reorganizations, and soil profiles.",
            pagesCount = 28,
            fileSize = "4.2 MB",
            isBookmarked = true,
            isDownloaded = true,
            isFavorite = true,
            dateAdded = "24 Sep 2026",
            contentPreview = "West Bengal is situated between 21°38' N and 27°10' N latitudes and 85°50' E and 89°50' E longitudes. The Teesta river originates from Pahunri glacier. Damodar was known as the 'Sorrow of Bengal' before DVC was established."
        ),
        StudyNote(
            id = "note_2",
            title = "Indian Economy: Five Year Plans, NITI Aayog & RBI Monetary Policy",
            examCategory = "PSC",
            subject = "Economics",
            description = "Detailed breakdown of Repo Rate, Reverse Repo, CRR, SLR, inflation indices (CPI/WPI), and fiscal deficit definitions.",
            pagesCount = 35,
            fileSize = "5.1 MB",
            isBookmarked = false,
            isDownloaded = false,
            isFavorite = true,
            dateAdded = "22 Sep 2026",
            contentPreview = "Monetary Policy Committee (MPC) consists of 6 members. The Reserve Bank of India was established on April 1, 1935 under the RBI Act 1934 on the recommendations of Hilton Young Commission."
        ),
        StudyNote(
            id = "note_3",
            title = "SSC CGL / CHSL High-Frequency English Idioms & One Word Substitution",
            examCategory = "SSC",
            subject = "English",
            description = "Over 450 most repeated one-word substitutions and idioms from 2018-2025 previous year papers with Bengali translations.",
            pagesCount = 42,
            fileSize = "3.8 MB",
            isBookmarked = true,
            isDownloaded = true,
            isFavorite = false,
            dateAdded = "20 Sep 2026",
            contentPreview = "1. Altruist - One who considers the happiness and well-being of others (পরোপকারী).\n2. Ephemeral - Lasting for a very short time (ক্ষণস্থায়ী).\n3. Burn the midnight oil - To study or work late into the night."
        ),
        StudyNote(
            id = "note_4",
            title = "Indian History: Revolt of 1857 to Indian Independence Act",
            examCategory = "WBCS",
            subject = "History",
            description = "Chronological timeline of Governor Generals, Viceroys, peasant uprisings (Santhal, Indigo), and Gandhian mass movements.",
            pagesCount = 50,
            fileSize = "6.5 MB",
            isBookmarked = false,
            isDownloaded = false,
            isFavorite = false,
            dateAdded = "19 Sep 2026",
            contentPreview = "The Revolt of 1857 started on 10th May 1857 at Meerut. Mangal Pandey mutinied at Barrackpore on 29 March 1857. Lord Canning was the Governor-General during the revolt and became the first Viceroy."
        )
    )

    val mockTests = listOf(
        MockTest(
            id = "test_wbcs_full_1",
            title = "WBCS Prelims 2026 — Full Length Mock Test 1",
            examCategory = "WBCS",
            testType = "Full Mock",
            questionsCount = 10, // Full preview set
            durationMinutes = 15,
            totalMarks = 20,
            difficulty = "Moderate",
            attemptsCount = 890,
            averageScore = 14.2
        ),
        MockTest(
            id = "test_ssc_cgl_1",
            title = "SSC CGL Tier-1: General Intelligence & Quantitative Aptitude",
            examCategory = "SSC",
            testType = "Subject Test",
            questionsCount = 8,
            durationMinutes = 12,
            totalMarks = 16,
            difficulty = "Challenging",
            attemptsCount = 640,
            averageScore = 11.5
        ),
        MockTest(
            id = "test_daily_quiz_today",
            title = "Today's Daily Rapid Quiz — 25 September 2026",
            examCategory = "WBCS",
            testType = "Daily Quiz",
            questionsCount = 5,
            durationMinutes = 5,
            totalMarks = 10,
            difficulty = "Easy",
            attemptsCount = 1420,
            averageScore = 8.1
        ),
        MockTest(
            id = "test_rrb_ntpc_1",
            title = "RRB NTPC CBT-1: General Awareness & Science Booster",
            examCategory = "RRB",
            testType = "Chapter Test",
            questionsCount = 6,
            durationMinutes = 10,
            totalMarks = 12,
            difficulty = "Moderate",
            attemptsCount = 510,
            averageScore = 9.0
        )
    )

    val testQuestions = listOf(
        // WBCS Prelims Test Questions
        TestQuestion(
            id = "q_1",
            testId = "test_wbcs_full_1",
            questionNumber = 1,
            questionText = "Which river in West Bengal is also famously known as the 'Sorrow of Bengal' due to historical flooding?",
            optionA = "Teesta",
            optionB = "Damodar",
            optionC = "Bhagirathi",
            optionD = "Subarnarekha",
            correctOptionIndex = 1,
            explanation = "Damodar was historically known as the 'Sorrow of Bengal' (বাংলার দুঃখ) because of its catastrophic monsoon floods before the Damodar Valley Corporation (DVC) multi-purpose river valley project was launched in 1948.",
            subject = "Geography"
        ),
        TestQuestion(
            id = "q_2",
            testId = "test_wbcs_full_1",
            questionNumber = 2,
            questionText = "Who presided over the historic 1906 Calcutta Session of the Indian National Congress where 'Swaraj' was proclaimed?",
            optionA = "Dadabhai Naoroji",
            optionB = "Gopal Krishna Gokhale",
            optionC = "Rash Behari Ghosh",
            optionD = "Bal Gangadhar Tilak",
            correctOptionIndex = 0,
            explanation = "Dadabhai Naoroji ('The Grand Old Man of India') presided over the 1906 Calcutta Session of the INC, where the goal of Swaraj (Self Government) was officially adopted by the Congress.",
            subject = "History"
        ),
        TestQuestion(
            id = "q_3",
            testId = "test_wbcs_full_1",
            questionNumber = 3,
            questionText = "Under which Article of the Constitution of India is the Public Service Commission for the Union and States established?",
            optionA = "Article 312",
            optionB = "Article 315",
            optionC = "Article 324",
            optionD = "Article 356",
            correctOptionIndex = 1,
            explanation = "Article 315 of the Indian Constitution provides for the establishment of a Union Public Service Commission (UPSC) for the Union and a State Public Service Commission (e.g. WBPSC) for each State.",
            subject = "Polity"
        ),
        TestQuestion(
            id = "q_4",
            testId = "test_wbcs_full_1",
            questionNumber = 4,
            questionText = "The Highest Peak of West Bengal is located in which hill range?",
            optionA = "Tiger Hill",
            optionB = "Sandakphu (Singalila Ridge)",
            optionC = "Ghum Hill",
            optionD = "Buxa Hill",
            correctOptionIndex = 1,
            explanation = "Sandakphu (3,636 meters / 11,930 ft) is the highest peak in West Bengal. It is located on the Singalila Ridge on the West Bengal - Nepal border.",
            subject = "Geography"
        ),
        TestQuestion(
            id = "q_5",
            testId = "test_wbcs_full_1",
            questionNumber = 5,
            questionText = "Which Five Year Plan in India was focused on the 'Mahalanobis Model' emphasizing rapid industrialization?",
            optionA = "First Five Year Plan",
            optionB = "Second Five Year Plan",
            optionC = "Third Five Year Plan",
            optionD = "Fourth Five Year Plan",
            correctOptionIndex = 1,
            explanation = "The Second Five Year Plan (1956-1961) was based on the Mahalanobis model, drafted by Indian statistician Prasanta Chandra Mahalanobis, and focused on heavy industry and capital goods.",
            subject = "Economics"
        ),

        // Daily Quiz Questions
        TestQuestion(
            id = "dq_1",
            testId = "test_daily_quiz_today",
            questionNumber = 1,
            questionText = "Who is the custodian of the Constitution of India?",
            optionA = "The President of India",
            optionB = "The Prime Minister of India",
            optionC = "The Supreme Court of India",
            optionD = "The Parliament of India",
            correctOptionIndex = 2,
            explanation = "The Supreme Court of India acts as the guardian and custodian of the Constitution of India.",
            subject = "Polity"
        ),
        TestQuestion(
            id = "dq_2",
            testId = "test_daily_quiz_today",
            questionNumber = 2,
            questionText = "In Birbhum district of West Bengal, Santiniketan is famous as the abode of learning founded by:",
            optionA = "Raja Ram Mohan Roy",
            optionB = "Rabindranath Tagore",
            optionC = "Ishwar Chandra Vidyasagar",
            optionD = "Swami Vivekananda",
            correctOptionIndex = 1,
            explanation = "Rabindranath Tagore established Visva-Bharati at Santiniketan in Birbhum district, which has also been inscribed as a UNESCO World Heritage site.",
            subject = "West Bengal GK"
        ),
        TestQuestion(
            id = "dq_3",
            testId = "test_daily_quiz_today",
            questionNumber = 3,
            questionText = "If A:B = 3:4 and B:C = 8:9, what is the ratio of A:C?",
            optionA = "2:3",
            optionB = "1:2",
            optionC = "3:2",
            optionD = "4:5",
            correctOptionIndex = 0,
            explanation = "A/C = (A/B) * (B/C) = (3/4) * (8/9) = 24/36 = 2/3. So ratio A:C = 2:3.",
            subject = "Quantitative Aptitude"
        )
    )

    val currentAffairs = listOf(
        CurrentAffair(
            id = "ca_1",
            title = "West Bengal Govt Expands 'Yuvasree' & Skill Development Initiatives",
            date = "25 Sep 2026",
            category = "West Bengal",
            summary = "New vocational training centres announced in Suri, Bolpur, and Durgapur industrial corridors to train 25,000 aspirants for competitive technical exams.",
            fullDetails = "The Government of West Bengal announced a specialized technical training grant across Birbhum and Burdwan districts. Aspirants preparing for SSC JE and Railway technical grades will benefit from direct access to polytechnic labs and competitive coaching subsidies."
        ),
        CurrentAffair(
            id = "ca_2",
            title = "Reserve Bank of India Keeps Repo Rate at 6.50% in Latest Bi-Monthly MPC Meeting",
            date = "24 Sep 2026",
            category = "Economy",
            summary = "Governor emphasizes retail inflation containment within the 4% target band while projecting steady GDP growth for FY2026-27.",
            fullDetails = "The six-member Monetary Policy Committee voted with a 5-1 majority to retain the key policy repo rate. Standing Deposit Facility (SDF) rate remains at 6.25% and Marginal Standing Facility (MSF) rate at 6.75%."
        ),
        CurrentAffair(
            id = "ca_3",
            title = "DRDO Successfully Test Fires Indigenous VSHORADS Missile System",
            date = "23 Sep 2026",
            category = "Defence",
            summary = "Very Short Range Air Defence System (VSHORADS) flight tested successfully off the coast of Odisha, demonstrating pinpoint intercept capability.",
            fullDetails = "The 4th generation man-portable air defence system incorporates dual-thrust rocket motors and miniaturized reaction control systems, vital for safeguarding border installations against low-altitude aerial threats."
        ),
        CurrentAffair(
            id = "ca_4",
            title = "ISRO Announces Shukrayaan Venus Mission Final Trajectory Milestones",
            date = "22 Sep 2026",
            category = "Science & Tech",
            summary = "India's planetary probe to Venus slated for 2028 window, carrying high-resolution Synthetic Aperture Radar (SAR) and atmospheric spectrometers.",
            fullDetails = "ISRO space scientists confirmed the payload configuration for exploring the dense, sulfuric acid atmosphere and surface volcanic geology of Venus."
        )
    )

    val examAlerts = listOf(
        ExamAlert(
            id = "alert_wbcs_2026",
            examName = "WBCS (Exe.) etc. Exam 2026",
            organization = "Public Service Commission, West Bengal (WBPSC)",
            vacancy = "850+ Projected Posts (Group A, B, C, D)",
            applyStartDate = "10 Oct 2026",
            applyEndDate = "05 Nov 2026",
            examDate = "14 Feb 2027 (Tentative Prelims)",
            eligibility = "Degree of a recognized University; Ability to read, write and speak in Bengali (or Nepali for hill candidates).",
            officialUrl = "https://wbpsc.gov.in",
            statusBadge = "Notification Released"
        ),
        ExamAlert(
            id = "alert_ssc_cgl_2026",
            examName = "SSC Combined Graduate Level (CGL) 2026",
            organization = "Staff Selection Commission (SSC)",
            vacancy = "17,727 Posts (Inspector, ASO, Tax Assistant)",
            applyStartDate = "24 Sep 2026",
            applyEndDate = "24 Oct 2026",
            examDate = "December 2026 (Tier-1)",
            eligibility = "Bachelor's Degree from a recognized University or equivalent. Age: 18–30 years.",
            officialUrl = "https://ssc.gov.in",
            statusBadge = "Online Application Live"
        ),
        ExamAlert(
            id = "alert_rrb_ntpc_2026",
            examName = "RRB NTPC Graduate & Under-Graduate Categories",
            organization = "Railway Recruitment Boards",
            vacancy = "11,558 Posts (Station Master, Goods Train Manager, Jr Clerk)",
            applyStartDate = "15 Sep 2026",
            applyEndDate = "15 Oct 2026",
            examDate = "Jan - Feb 2027",
            eligibility = "12th Pass / Graduate based on post applied. Medical Standard A-2 for Station Master.",
            officialUrl = "https://indianrailways.gov.in",
            statusBadge = "Application Closing Soon"
        ),
        ExamAlert(
            id = "alert_wb_tet_2026",
            examName = "West Bengal Primary TET 2026",
            organization = "West Bengal Board of Primary Education (WBBPE)",
            vacancy = "12,000+ Primary Teachers",
            applyStartDate = "01 Nov 2026",
            applyEndDate = "25 Nov 2026",
            examDate = "27 Dec 2026",
            eligibility = "Higher Secondary with at least 50% marks and 2-year D.El.Ed / 4-year B.El.Ed.",
            officialUrl = "https://wbbpe.org",
            statusBadge = "Upcoming Gazette"
        )
    )

    val courseBatches = listOf(
        CourseBatch(
            id = "course_wbcs_target",
            title = "WBCS 2026-27 Integrated Foundation Batch",
            exam = "WBCS",
            duration = "12 Months",
            faculty = "Senior Ex-WBCS Officers & WBCS Mentors",
            price = "₹4,999",
            originalPrice = "₹12,000",
            isEnrolled = true,
            rating = 4.95,
            lecturesCount = 280
        ),
        CourseBatch(
            id = "course_ssc_rrb_combo",
            title = "SSC CGL + RRB NTPC Super Combined Batch",
            exam = "SSC & Railway",
            duration = "6 Months",
            faculty = "Er. Subhajit Roy & Science Faculty",
            price = "₹2,999",
            originalPrice = "₹7,500",
            isEnrolled = false,
            rating = 4.88,
            lecturesCount = 190
        ),
        CourseBatch(
            id = "course_banking_pro",
            title = "IBPS / SBI PO & Clerk Banking Elite Batch",
            exam = "BANKING",
            duration = "5 Months",
            faculty = "Priyanka Roy & Quant Experts",
            price = "₹3,499",
            originalPrice = "₹8,000",
            isEnrolled = false,
            rating = 4.92,
            lecturesCount = 160
        ),
        CourseBatch(
            id = "course_wb_tet_spe",
            title = "WB Primary & Upper Primary TET Success Batch",
            exam = "TET",
            duration = "4 Months",
            faculty = "Tanmoy Mondal & Child Psychology Team",
            price = "₹1,999",
            originalPrice = "₹5,000",
            isEnrolled = false,
            rating = 4.85,
            lecturesCount = 110
        )
    )

    val initialNotifications = listOf(
        NotificationItem(
            id = "notif_1",
            title = "🔴 Live Class Starting: West Bengal Geography",
            message = "Prof. Anirban Sen has begun the WBCS Geography live session. Click to join room now.",
            category = "Class",
            timestamp = "10 mins ago",
            isRead = false
        ),
        NotificationItem(
            id = "notif_2",
            title = "🚨 SSC CGL 2026 Vacancies Increased to 17,727",
            message = "Staff Selection Commission has updated tentative vacancies. Last date to apply is 24 Oct 2026.",
            category = "Alert",
            timestamp = "2 hours ago",
            isRead = false
        ),
        NotificationItem(
            id = "notif_3",
            title = "📖 New Notes Uploaded: Indian Economy Monetary Policy",
            message = "Comprehensive 35-page PDF with previous year questions added to PSC folder.",
            category = "Note",
            timestamp = "1 day ago",
            isRead = true
        ),
        NotificationItem(
            id = "notif_4",
            title = "🏆 Weekly WBCS Mock Test Rank Published",
            message = "Congratulations! You secured Rank #14 in Birbhum-Burdwan zone with 88% accuracy.",
            category = "Announcement",
            timestamp = "2 days ago",
            isRead = true
        )
    )

    val initialAttendance = listOf(
        AttendanceRecord(date = "25 Sep 2026", subject = "Geography & Current Affairs", isPresent = true, remarks = "Live Class Attended"),
        AttendanceRecord(date = "24 Sep 2026", subject = "Quantitative Aptitude", isPresent = true, remarks = "Classroom at Suri Branch"),
        AttendanceRecord(date = "23 Sep 2026", subject = "Indian Polity & Constitution", isPresent = true, remarks = "Classroom at Suri Branch"),
        AttendanceRecord(date = "22 Sep 2026", subject = "Modern History of India", isPresent = false, remarks = "Medical Leave"),
        AttendanceRecord(date = "21 Sep 2026", subject = "Reasoning & Analytical Logic", isPresent = true, remarks = "Classroom at Suri Branch"),
        AttendanceRecord(date = "20 Sep 2026", subject = "Weekly Mock Test Evaluation", isPresent = true, remarks = "Test Center Suri")
    )

    val initialDoubts = listOf(
        Doubt(
            studentName = "Sourav Mukherjee",
            subject = "Geography",
            questionText = "Sir, what is the exact difference between Chhotanagpur plateau extension in Purulia and the Rarh plains in Bankura/Birbhum?",
            status = "Answered",
            replyText = "Purulia represents the eroded western undulating plateau fringe with Monadnocks (like Ayodhya & Baghmundi hills). Rarh plain on the other hand is formed by older lateritic alluvium deposited by rivers like Mayurakshi, Ajay, and Damodar.",
            teacherName = "Prof. Anirban Sen"
        ),
        Doubt(
            studentName = "Sourav Mukherjee",
            subject = "Quantitative Aptitude",
            questionText = "In Compound Interest, how to quickly find the difference between CI and SI for 3 years without long cubic expansion?",
            status = "Answered",
            replyText = "Use the direct competitive formula: Difference = P * (R/100)^2 * (3 + R/100). For 2 years it is simply P*(R/100)^2.",
            teacherName = "Er. Subhajit Roy"
        )
    )
}
