<br />
<p align="center">
  <a href="https://github.com/vswati0101/EventsApp/assets/140166757/7c913be9-7e3e-49f2-8122-8c4b6205dc60">
    <img src="https://github.com/vswati0101/EventsApp/assets/140166757/7c913be9-7e3e-49f2-8122-8c4b6205dc60" alt="Logo" width="80" height="80">
  </a>
  <h3 align="center">EVENTSAPP</h3>
  
## Overview:
Events App is an Android application developed in Kotlin that allows users to explore and discover various events. The app provides features such as user authentication, event listing, event details, social media integration, wishlist functionality, and user profile management.

## Features:
- **User Authentication:** Users can login or signup using their credentials. Firebase is utilized for authentication.
- **Event Listing:** Home page displays a list of events fetched from the Ticketmaster API using Retrofit.
- **Event Details:** Users can view detailed information about an event by clicking on it. The details page includes a collapsing toolbar for an enhanced user experience.
- **Social Media Integration:** Users can access the Instagram, Facebook, or YouTube pages of an event directly from the event details page.
- **Wishlist Functionality:** Users can add events to their wishlist, which is stored locally using Room Database. The wishlist button changes color to indicate whether an event is added or removed from the wishlist.
- **Wishlist Fragment:** A dedicated fragment displays the list of events added to the user's wishlist. Events can be removed from the wishlist by swiping left or right or by clicking the wishlist button again.
- **User Profile Management:** Users can update their password or logout from the profile page.

## Architecture:
The application follows the MVVM (Model-View-ViewModel) architecture pattern for better separation of concerns and maintainability.

## Technologies Used:
- Kotlin
- Firebase Authentication
- Retrofit
- Ticketmaster API
- Room Database
- Collapsing Toolbar

## Setup Instructions:
1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Make sure to have the necessary dependencies installed and configured.
4. Run the application on an emulator or physical device.

## Screenshots:
![Events App(2)](https://github.com/vswati0101/FamSafety/assets/140166757/3560a2b6-82c0-4b9f-b267-5d431b322414)

![Events App(3)](https://github.com/vswati0101/FamSafety/assets/140166757/0b1a31a7-ae47-4652-a1ff-c0ad7337ac59)


## Contribution Guidelines:
- Fork the repository.
- Create a new branch for your feature or bug fix.
- Make your changes and test thoroughly.
- Submit a pull request detailing the changes made.

## Contact:
For any inquiries or support, please contact [vswati0101@email.com].

