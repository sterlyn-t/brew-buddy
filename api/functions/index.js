/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

// The Cloud Functions for Firebase SDK to create Cloud Functions and triggers.
// const { logger } = require("firebase-functions");
const {
  log,
  info,
  debug,
  warn,
  error,
  write,
} = require("firebase-functions/logger");

const {
  onCall,
  HttpsError,
  onRequest,
} = require("firebase-functions/v2/https");
const { initializeApp } = require("firebase-admin/app");
const { getFirestore } = require("firebase-admin/firestore");

const {
  getRecipeById,
  getRecipes,
  getRecipesMetadata,
  getRecipeMetadataById,
  getRecipesMetadataByQuery,
} = require("./utils/recipes.js");
const { getUserById, updatePinnedRecipes } = require("./utils/users.js");

initializeApp();
const db = getFirestore();

const DEFAULT_BANNER_URL =
  "https://firebasestorage.googleapis.com/v0/b/brew-buddy-ece452.appspot.com/o/placeholder_banner.png?alt=media&token=49e30f3c-cc2d-44f4-a91a-a1295f558a6a";
const DEFAULT_AVATAR_URL =
  "https://firebasestorage.googleapis.com/v0/b/brew-buddy-ece452.appspot.com/o/placeholder_avatar.jpg?alt=media&token=38f93e98-58d1-4076-8262-1dc5c340cac7";

exports.getRecipesByAuthor = onCall(async ({ data }, context) => {
  // Get all recipes from specified author ID.
  var recipes = [];
  const { authorId } = data;

  if (!authorId) {
    // Throwing an HttpsError so that the client gets the error details.
    throw new HttpsError("failed-precondition", "No author ID provided");
  }
  await db
    .collection("recipes")
    .where("authorId", "==", authorId)
    .get()
    .then((snapshot) => {
      snapshot.forEach((doc) => {
        const data = {
          id: doc.id,
          author: doc.data().author,
          ingredients: doc.data().ingredients,
        };
        recipes.push(data);
      });
    });
  return recipes;
});

exports.getRecipeById = onCall(async ({ data }, context) => {
  console.log("GET RECIPE BY ID");
  const { recipeId } = data;
  log("getRecipeById Request: ", data);
  console.log("getRecipeById Request: ", data);

  if (!recipeId) {
    // Throwing an HttpsError so that the client gets the error details.
    throw new HttpsError("failed-precondition", "No recipe ID provided");
  }
  var recipe = await getRecipeById(recipeId, db);

  var author = null;
  try {
    author = await getUserById(recipe.authorId, db);
  } catch (e) {
    author = null;
  }

  console.log("Author of GetRecipeById: ", author);
  return {
    ...recipe,
    author,
  };
});

exports.getUserById = onCall(async ({ data }, context) => {
  const { userId } = data;
  log("getUserById Request: ", data);
  log("getUserById userId: ", userId);

  if (userId === undefined) {
    // Throwing an HttpsError so that the client gets the error details.
    throw new HttpsError(
      "failed-precondition",
      "getUserById: No user ID provided"
    );
  }
  const user = await getUserById(userId, db);
  console.log("getUserById: ", user);
  return user;
});

exports.pinRecipe = onCall(async ({ data }, context) => {
  const { userId, recipeId } = data;
  log("pinRecipe Request: ", data);

  if (userId === undefined || userecipeIdrId === undefined) {
    // Throwing an HttpsError so that the client gets the error details.
    throw new HttpsError(
      "failed-precondition",
      "pinRecipe: No user or recipe ID provided"
    );
  }
  const user = await getUserById(userId, db);
  const pinnedRecipes = user.pinnedRecipes || [];
  if (pinnedRecipes.includes(recipeId)) {
    // the recipe is already pinned, so unpin it
    updatePinnedRecipes(
      userId,
      pinnedRecipes.filter((id) => id !== recipeId),
      db
    );
  } else {
    updatePinnedRecipes(userId, [...pinnedRecipes, recipeId], db);
  }
});

const getRecipesMetadataWithAuthor = async (metadatas, db) => {
  const res = [];
  for (let i = 0; i < metadatas.length; i++) {
    const metadata = metadatas[i];
    if (metadata.authorId) {
      const author = await getUserById(metadata.authorId, db);
      res.push({
        id: metadata.id,
        bannerUrl: metadata.bannerUrl,
        likes: metadata.likes,
        title: metadata.title,
        tags: metadata.tags,
        author,
      });
    }
  }
  return res;
};

const getQueryParams = (string) => {
  const arr = string.split("&");

  const query = { keywords: [], filters: {} };

  arr.forEach((el) => {
    const entry = el.split("=");
    if (entry[0] === "keywords") {
      query["keywords"] = entry[1].toLowerCase().split(" ");
    } else if (entry[0] === "sort") {
      query["sort"] = entry[1];
    } else {
      query["filters"][entry[0]] = entry[1] === "true" ? true : false;
    }
  });
  return query;
};
exports.getRecipesMetadata = onCall(async ({ data }, context) => {
  const { query } = data || {};
  if (query) {
    const queryParams = getQueryParams(query);
    console.log(queryParams);
    const metadatas = await getRecipesMetadataByQuery(
      queryParams.keywords,
      queryParams.filters,
      queryParams.sort,
      db
    );
    // console.log(metadatas);
    return await getRecipesMetadataWithAuthor(metadatas, db);
  }
  const metadatas = await getRecipesMetadata(db);
  return await getRecipesMetadataWithAuthor(metadatas, db);
});

exports.getPopularRecipes = onCall(async ({ data }, context) => {
  const metadatas = await getRecipesMetadata(db);

  const popularRecipes = await getRecipesMetadataWithAuthor(metadatas, db);

  popularRecipes.sort((a, b) => b.likes - a.likes);
  console.log("Popular Recipes: ", popularRecipes);
  return popularRecipes.slice(0, 5);
});

exports.getFeaturedRecipes = onCall(async ({ data }, context) => {
  const metadatas = await getRecipesMetadata(db);
});

exports.getUserPreferences = onCall(async ({ data }, context) => {
  const { userId } = data;
  const prefs = await this.getUserPreferences(userId, db);
  return prefs;
});

// exports.createRecipe = onRequest(async ({ body }, response) => {
//   console.log(body);
//   const { recipes, users } = body;
//   recipes.forEach((recipe) => {
//     db.collection("recipes").doc().set(recipe);
//   });

//   users.forEach((user) => {
//     db.collection("users").doc(user["uid"]).set(user);
//   });
//   // const res = await db.collection("recipes").doc().set(body);
//   response.status(200).send();
// });

exports.updateRecipes = onRequest(async ({ body }, response) => {
  const blacklistWords = [
    "as",
    "the",
    "is",
    "at",
    "in",
    "with",
    "a",
    "&",
    "and",
    "to",
    "how",
    "you",
    "all",
  ];
  await db
    .collection("recipes")
    .get()
    .then((snapshot) => {
      snapshot.forEach((doc) => {
        const titleWords = doc.data().title.toLowerCase().split(" ");

        const keywords = titleWords.filter(
          (w) => !blacklistWords.includes(w.toLowerCase())
        );

        // const preferences = {
        //   glutenFree: doc.data().glutenFree,
        //   vegetarian: doc.data().vegetarian,
        //   vegan: doc.data().vegan,
        //   dairyFree: doc.data().dairyFree,
        //   keto: doc.data().keto,
        // };

        // const preferenceArray = Object.keys(preferences).filter(
        //   (a) => preferences[a] === true
        // );
        // console.log(preferenceArray);
        db.collection("recipes").doc(doc.id).update({ keywords });
      });
    });

  response.status(200).json({ data: "yay" });
});
const functions = require("firebase-functions");
const admin = require("firebase-admin");

exports.notifyUserOnNewItem = functions.firestore
  .document("saved_stores/{userId}")
  .onUpdate((change, context) => {
    // Get the updated data from the snapshot
    const updatedData = change.after.data();
    const previousData = change.before.data();

    // Compare the previous items array with the updated items array
    const previousItems = previousData.items;
    const updatedItems = updatedData.items;

    if (!previousItems || !updatedItems) {
      // Items array does not exist, nothing to compare, exit
      return null;
    }

    if (previousItems.length === updatedItems.length) {
      // The array length is the same, no new item added, exit
      return null;
    }

    // Find the new item added to the array
    const newItem = updatedItems.filter(
      (item) => !previousItems.includes(item)
    )[0];

    if (!newItem) {
      // New item not found, exit
      return null;
    }

    // Get the user ID from the context (document ID is the user ID)
    const userId = context.params.userId;

    // Create a notification payload
    const payload = {
      notification: {
        title: "New Item Added!",
        body: `A new item "${newItem}" is added to your saved stores.`,
        click_action: "MAIN_ACTIVITY", // Adjust this to the activity you want to open when the notification is clicked
      },
    };

    // Send the notification to the user
    return admin.messaging().sendToDevice(userId, payload);
  });
