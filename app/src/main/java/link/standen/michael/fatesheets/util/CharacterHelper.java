package link.standen.michael.fatesheets.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import link.standen.michael.fatesheets.R;
import link.standen.michael.fatesheets.model.CoreCharacter;

/**
 * A helper class for managing characters
 */
public final class CharacterHelper {

	private static final String TAG = CharacterHelper.class.getName();

	private static final String CORE_PREFIX = "Core_";

	private CharacterHelper(){}

	/**
	 * Returns the default name for a character.
	 */
	@NonNull
	public static String getCharacterDefaultName(Context context){
		return context.getResources().getString(R.string.character_name_default);
	}

	/**
	 * Saves the character to local storage.
	 * @return True if the write operation was successful, false otherwise.
	 */
	public static boolean saveCoreCharacter(Context context, CoreCharacter character) {
		String json = new Gson().toJson(character);
		try {
			FileOutputStream fos = context.openFileOutput(CORE_PREFIX + character.getName(), Context.MODE_PRIVATE);
			fos.write(json.getBytes());
			fos.close();
			return true;
		} catch (IOException e) {
			Log.e(TAG, "Error writing character", e);
		}
		return false;
	}

	/**
	 * Get a saved character.
	 */
	@Nullable
	public static CoreCharacter getCoreCharacter(Context context, String name) {
		if (name == null) {
			return null;
		}
		name = CORE_PREFIX + name;

		String json = null;

		try {
			FileInputStream fis = context.openFileInput(name);

			StringBuffer stringBuff = new StringBuffer("");
			byte[] buff = new byte[1024];
			int n;
			while ((n = fis.read(buff)) != -1) {
				stringBuff.append(new String(buff, 0, n));
			}
			fis.close();

			json = stringBuff.toString();
		} catch (FileNotFoundException e) {
			Log.e(TAG, String.format("Character %s file not found", name), e);
		} catch (IOException e) {
			Log.e(TAG, String.format("Error reading file %s", name), e);
		}

		if (json == null) {
			return null;
		}
		return new Gson().fromJson(json, CoreCharacter.class);
	}

	/**
	 * Get a list of all saved characters names.
	 */
	public static List<String> listCharacterNames(Context context) {
		List<String> names = new ArrayList<>();

		for (String name : context.fileList()){
			if (name.startsWith(CORE_PREFIX)){
				names.add(name.replace(CORE_PREFIX, ""));
			}
		}

		return names;
	}
}
