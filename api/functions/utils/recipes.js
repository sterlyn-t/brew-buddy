const { HttpsError } = require("firebase-functions/v2/https");

exports.getRecipeById = (recipeId, db) => {
  if (!recipeId) {
    throw new HttpsError(
      "failed-precondition",
      "getRecipeById: No recipe ID provided"
    );
  }
  return db
    .collection("recipes")
    .doc(recipeId)
    .get()
    .then((doc) => {
      if (!doc) {
        throw new HttpsError(
          "failed-precondition",
          `No recipe with ID ${recipeId} found`
        );
      }
      return {
        id: doc.id,
        ...doc.data(),
      };
    });
};

exports.getRecipeMetadataById = (recipeId, db) => {
  if (!recipeId) {
    throw new HttpsError(
      "failed-precondition",
      "getRecipeMetadataById: No recipe ID provided"
    );
  }
  return db
    .collection("recipes")
    .doc(recipeId)
    .get()
    .then((doc) => {
      if (!doc) {
        throw new HttpsError(
          "failed-precondition",
          `No recipe with ID ${recipeId} found`
        );
      }
      return {
        id: doc.id,
        bannerUrl: doc.data().bannerUrl,
        title: doc.data().title,
      };
    });
};

exports.getRecipesByAuthor = (authorId, db) => {
  if (!authorId) {
    throw new HttpsError(
      "failed-precondition",
      "getRecipesByAuthor: No author ID provided"
    );
  }
  return db
    .collection("recipes")
    .where("authorId", "==", authorId)
    .get()
    .then((snapshot) => {
      snapshot.map((doc) => {
        return {
          id: doc.id,
          ...doc.data(),
        };
      });
    });
};

exports.getRecipes = (db) => {
  return db
    .collection("recipes")
    .get()
    .then((snapshot) => {
      return snapshot.docs.map((doc) => {
        return {
          id: doc.id,
          ...doc.data(),
        };
      });
    });
};



exports.getRecipesMetadata = (db) => {
  return db
    .collection("recipes")
    .get()
    .then((snapshot) => {
      const metadata = snapshot.docs.map((doc) => ({
        id: doc.id,
        bannerUrl: doc.data().bannerUrl,
        title: doc.data().title,
        likes: doc.data().likes,
        authorId: doc.data().authorId,
        vegan: doc.data().vegan,
        vegetarian: doc.data().vegetarian,
        glutenFree: doc.data().glutenFree,
        dairyFree: doc.data().dairyFree
      }));
      console.log("here: ", metadata);
      return metadata
    });
};

exports.getRecipesMetadataByQuery = (keywords, filters, db) => {
  const queryRef = db
    .collection("recipes")
    .where("keywords", "array-contains-any", keywords);

  for (const [key, value] of Object.entries(filters)) {
    // if filter is not true, then don't need to filter it
    if (value) {
      queryRef.where(key, "==", value);
    }
  }

  return queryRef.get().then((snapshot) => {
    return snapshot.docs.map((doc) => ({
      id: doc.id,
      bannerUrl: doc.data().bannerUrl,
      title: doc.data().title,
      likes: doc.data().likes,
      authorId: doc.data().authorId,
    }));
  });
};
