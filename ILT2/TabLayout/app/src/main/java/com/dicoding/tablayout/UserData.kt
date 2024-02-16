package com.dicoding.tablayout

object UserData {

    private val usersName = arrayOf(
        "Clara Bright",
        "Ethan Walker",
        "Maya Sanchez",
        "Liam Carter",
        "Sophia Lee",
        "Noah Miller",
        "Olivia Brown",
        "William Johnson",
        "Isabella Garcia",
        "Mason Davis",
        "Evelyn Hernandez",
        "Lucas Wilson",
        "Mia Moore",
        "Benjamin Adams",
        "Amelia Torres",
        "Aiden Lewis",
        "Charlotte Robinson",
        "Logan Walker",
        "Harper Garcia",
        "Elijah King"
    )

    // Images source: https://www.figma.com/community/file/1099697193260511631/avatar-pack-profile-pic-sample-including-ai-generated-faces
    private val usersImage = arrayOf(
        R.drawable.user1,
        R.drawable.user2,
        R.drawable.user3,
        R.drawable.user4,
        R.drawable.user5,
        R.drawable.user6,
        R.drawable.user7,
        R.drawable.user8,
        R.drawable.user9,
        R.drawable.user10,
        R.drawable.user11,
        R.drawable.user12,
        R.drawable.user13,
        R.drawable.user14,
        R.drawable.user15,
        R.drawable.user16,
        R.drawable.user17,
        R.drawable.user18,
        R.drawable.user19,
        R.drawable.user20,
    )

    fun getUserFromID(userID: Int): User{
        return User(
            id = userID,
            name = usersName[userID],
            image = usersImage[userID]
        )
    }

}