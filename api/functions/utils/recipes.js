const { HttpsError } = require("firebase-functions/v2/https");

const sortingEquations = {
  likesAsce: (a, b) => a.likes - b.likes,
  likesDesc: (a, b) => b.likes - a.likes,
};
// const sortRecipes = (param, recipes) => {
//   return recipes.sort(sortingEquations[param]);
// };
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
      return extractMetadata(doc);
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

const extractMetadata = (doc) => ({
  id: doc.id,
  bannerUrl: doc.data().bannerUrl,
  title: doc.data().title,
  likes: doc.data().likes,
  authorId: doc.data().authorId,
  tags: doc.data().tags,
});

exports.getRecipesMetadata = (db) => {
  return db
    .collection("recipes")
    .get()
    .then((snapshot) => {
      return snapshot.docs.map((doc) => extractMetadata(doc));
    });
};

exports.getRecipesMetadataByQuery = (keywords, filters, sort, db) => {
  let queryRef = db.collection("recipes");
  console.log("keywords: ", keywords);
  if (keywords.length > 0) {
    queryRef = queryRef.where("keywords", "array-contains-any", keywords);
  }

  for (const [key, value] of Object.entries(filters)) {
    // if filter is not true, then don't need to filter it
    console.log(`key: ${key} value: ${value}`);
    if (value) {
      queryRef = queryRef.where(key, "==", value);
    }
  }

  return queryRef.get().then((snapshot) => {
    const res = snapshot.docs.map((doc) => extractMetadata(doc));
    if (sort) {
      res.sort(sortingEquations[sort]);
    }
    // console.log("Res: ", res);
    return res;
  });
};
