package com.example.brewbuddy.data.repository
import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.data.remote.dto.Instructions
import com.example.brewbuddy.data.remote.dto.MarketplaceItemDto
import com.example.brewbuddy.data.remote.dto.MarketplaceItemMetadataDto
import com.example.brewbuddy.data.remote.dto.RecipeDto
import com.example.brewbuddy.data.remote.dto.RecipeMetadataDto
import com.example.brewbuddy.data.remote.dto.UserDto
import com.example.brewbuddy.domain.repository.RecipeRepository
import com.example.brewbuddy.requests.getFunctions
import com.example.brewbuddy.domain.model.Author
import com.example.brewbuddy.domain.model.Recipe
import com.example.brewbuddy.domain.model.User
import com.example.brewbuddy.profile.db
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepositoryImplementation @Inject constructor () : RecipeRepository {

    override suspend fun getRecipes(query: String?): List<RecipeMetadataDto> {
        Log.d("GET_RECIPES", "Running")
        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                if(query != null) {
                    getFunctions()
                        .getHttpsCallable("getRecipesMetadata")
                        .call(hashMapOf("query" to query)).await()

                } else {
                    getFunctions()
                        .getHttpsCallable("getRecipesMetadata")
                        .call().await()
                }
            }
            val task = dataDeferred.await()
            val data = task.data as List<HashMap<String, Object>>
            return@withContext data.map{RecipeMetadataDto.from(it)}
        }
    }
    override suspend fun getRecipeById(id: String): RecipeDto {
        Log.d("GET_RECIPE_BY_ID", id)

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                getFunctions()
                    .getHttpsCallable("getRecipeById")
                    .call(hashMapOf("recipeId" to id)).await()
            }
            val task = dataDeferred.await()
            val data = task.data as HashMap<String, Object>
            return@withContext RecipeDto.from(data)
        }
    }
    override suspend fun getPopular(): List<RecipeMetadataDto> {
        Log.d("GET_POPULAR", "Running")

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                getFunctions()
                    .getHttpsCallable("getPopularRecipes")
                    .call().await()
            }
            val task = dataDeferred.await()
            val data = task.data as List<HashMap<String, Object>>
            return@withContext data.map{RecipeMetadataDto.from(it)}
        }
    }
    override suspend fun getRecipesByUserId(user_id: String): List<RecipeMetadataDto> {
        Log.d("GET_RECIPES_BY_USER_ID", user_id)

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                getFunctions()
                    .getHttpsCallable("getUserRecipes")
                    .call(hashMapOf("authorId" to user_id)).await()
            }
            Log.d("GET_RECIPES_BY_USER_ID2", user_id)
            val task = dataDeferred.await()
            Log.d("GET_RECIPES_BY_USER_ID3", user_id)
            val data = task.data as List<HashMap<String, Object>>
            Log.d("GET_RECIPES_BY_USER_ID4", user_id)
            return@withContext data.map{RecipeMetadataDto.from(it)}

        }
    }
    override suspend fun getRecommended(userId: String): List<RecipeMetadataDto> {
        Log.d("GET_RECOMMENDED", "Running")

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                getFunctions()
                    .getHttpsCallable("getRecommendedRecipes")
                    .call(hashMapOf("userId" to userId)).await()
            }
            val task = dataDeferred.await()
            val data = task.data as List<HashMap<String, Object>>
            Log.d("RECOMMENDED", "$data")
            return@withContext data.map{RecipeMetadataDto.from(it)}
        }
    }
    override suspend fun getMarketplaceItems(query: String?): List<MarketplaceItemMetadataDto> {
        Log.d("GET_MARKETPLACE_ITEMS", "Running")

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                if(query != null) {
                    getFunctions()
                        .getHttpsCallable("getMarketplaceItemsMetadata")
                        .call(hashMapOf("query" to query)).await()

                } else {
                    getFunctions()
                        .getHttpsCallable("getMarketplaceItemsMetadata")
                        .call().await()
                }
            }

            val task = dataDeferred.await()
            val data = task.data as List<HashMap<String, Object>>
            return@withContext data.map{MarketplaceItemMetadataDto.from(it)}
        }
    }
    override suspend fun getMarketplaceItemById(id: String): MarketplaceItemDto {
        Log.d("GET_MARKETPLACE_ITEM_BY_ID", id)

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                getFunctions()
                    .getHttpsCallable("getMarketplaceItemById")
                    .call(hashMapOf("itemId" to id)).await()
            }
            val task = dataDeferred.await()
            val data = task.data as HashMap<String, Object>
            return@withContext MarketplaceItemDto.from(data)
        }
    }

    override suspend fun getMarketplaceItemsByUserId(userId: String): List<MarketplaceItemMetadataDto> {
        Log.d("GET_MARKETPLACE_ITEM_BY_USER_ID", userId)

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                getFunctions()
                    .getHttpsCallable("getMarketplaceItemsByUserId")
                    .call(hashMapOf("userId" to userId)).await()
            }
            val task = dataDeferred.await()
            val data = task.data as List<HashMap<String, Object>>
            return@withContext data.map{MarketplaceItemMetadataDto.from(it)}
        }
    }

    override suspend fun signIn(username: String, password: String): Boolean {
        val auth = Firebase.auth
        val db = FirebaseFirestore.getInstance()

        val usersCollection = db.collection("users")

        return withContext(Dispatchers.IO) {
            val querySnapshotDeferred = async { usersCollection.whereEqualTo("username", username).get().await() }
            val querySnapshot = querySnapshotDeferred.await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents[0]
                val email = document.getString("email")

                if (email != null) {
                    val authResultDeferred = async { auth.signInWithEmailAndPassword(email, password).await() }
                    val authResult = authResultDeferred.await()

                    if (authResult != null) {
                        val user = auth.currentUser
                        return@withContext true
                    }
                }
            }

            return@withContext false
        }
    }
    override suspend fun registerUserWithGoogle(context: Context, username: String, email: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val account = GoogleSignIn.getLastSignedInAccount(context)
                if (account == null) {
                    return@withContext false
                }

                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                val authResult = FirebaseAuth.getInstance().signInWithCredential(credential).await()
                val currentUser = authResult.user

                if(currentUser == null) {
                    return@withContext false;
                }
                val documentRef = db.collection("users").document(currentUser!!.uid)
                val firestoreUser = documentRef.get().await()
                if(!firestoreUser.exists()) {
                    val user = hashMapOf(
                        "email" to account.email,
                        "username" to account.displayName,
                        "uid" to currentUser!!.uid,
                        "avatarUrl" to account.photoUrl.toString(),
                    )
                    documentRef.set(user).await()
                    return@withContext true
                }
                return@withContext currentUser != null

            } catch(e: Exception) {
                return@withContext false
            }

        }
    }
    override suspend fun getUserById(userId: String) : UserDto {
        Log.d("GET_USER_BY_ID", userId)

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                getFunctions()
                    .getHttpsCallable("getUserById")
                    .call(hashMapOf("userId" to userId)).await()
            }
            val task = dataDeferred.await()
            val data = task.data as HashMap<String, Object>
            return@withContext UserDto.from(data)
        }

    }
}