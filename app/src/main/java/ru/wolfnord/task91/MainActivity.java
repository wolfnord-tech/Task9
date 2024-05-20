package ru.wolfnord.task91;

// Импорт необходимых библиотек
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

// Основной класс MainActivity, наследующий AppCompatActivity
public class MainActivity extends AppCompatActivity {

    // Объявление переменных для ввода и вывода текста
    private EditText editTextFileName;
    private EditText editTextFileContent;
    private TextView textViewFileContent;

    // Ключи для сохранения состояния
    private static final String KEY_FILE_NAME = "fileName";
    private static final String KEY_FILE_CONTENT = "fileContent";
    private static final String KEY_TEXT_VIEW_CONTENT = "textViewContent";

    // Метод onCreate вызывается при создании активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Привязка переменных к элементам интерфейса
        editTextFileName = findViewById(R.id.editTextName);
        editTextFileContent = findViewById(R.id.editTextContent);
        textViewFileContent = findViewById(R.id.textView);

        // Привязка кнопок к элементам интерфейса и установка слушателей на нажатие кнопок
        Button buttonCreateFile = findViewById(R.id.createFileButton);
        Button buttonReadFile = findViewById(R.id.readFileButton);
        Button buttonAppendToFile = findViewById(R.id.addFileButton);
        Button buttonDeleteFile = findViewById(R.id.deleteFileButton);

        // Установка действий на кнопки
        buttonCreateFile.setOnClickListener(v -> createFile());
        buttonReadFile.setOnClickListener(v -> readFile());
        buttonAppendToFile.setOnClickListener(v -> appendToFile());
        buttonDeleteFile.setOnClickListener(v -> deleteCustomFile(editTextFileName.getText().toString()));
    }

    // Сохранение состояния при повороте экрана
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_FILE_NAME, editTextFileName.getText().toString());
        outState.putString(KEY_FILE_CONTENT, editTextFileContent.getText().toString());
        outState.putString(KEY_TEXT_VIEW_CONTENT, textViewFileContent.getText().toString());
    }

    // Восстановление состояния после поворота экрана
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        editTextFileName.setText(savedInstanceState.getString(KEY_FILE_NAME));
        editTextFileContent.setText(savedInstanceState.getString(KEY_FILE_CONTENT));
        textViewFileContent.setText(savedInstanceState.getString(KEY_TEXT_VIEW_CONTENT));
    }

    // Создание файла
    private void createFile() {
        String fileName = editTextFileName.getText().toString() + ".txt";
        String fileContent = editTextFileContent.getText().toString();

        // Путь к директории
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Files");

        if (!directory.exists()) {
            directory.mkdirs(); // Создание директории, если она не существует
        }

        File file = new File(directory, fileName);

        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(fileContent.getBytes()); // Запись данных в файл
            outputStream.close(); // Закрытие файла
            showToast("Файл создан"); // Вывод сообщения об успешном создании файла
        } catch (Exception e) {
            Log.e("FileError", "Exception - create"); // Логирование ошибки
        }
    }

    // Чтение файла
    private void readFile() {
        String fileName = editTextFileName.getText().toString() + ".txt";
        StringBuilder fileContent = new StringBuilder();

        try {
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Files");
            File file = new File(directory, fileName);

            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                fileContent.append(line).append("\\n"); // Чтение данных из файла
            }

            // Вывод содержимого файла
            textViewFileContent.setText(fileContent.toString());

            fileInputStream.close(); // Закрытие файла
        } catch (IOException e) {
            Log.e("FileError", "Exception - read"); // Логирование ошибки
        }
    }

    // Добавление данных в файл
    private void appendToFile() {
        String fileName = editTextFileName.getText().toString() + ".txt";
        String fileContent = editTextFileContent.getText().toString();

        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Files");
        if (!directory.exists()) {
            directory.mkdirs(); // Создание директории, если она не существует
        }

        File file = new File(directory, fileName);

        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(file, true); // Добавление данных в конец файла
            outputStream.write(fileContent.getBytes()); // Запись данных в файл
            outputStream.close(); // Закрытие файла
            showToast("Данные добавлены в файл"); // Вывод сообщения о успешном добавлении данных
        } catch (Exception e) {
            Log.e("FileError", "Exception - append"); // Логирование ошибки
        }
    }

    // Удаление файла
    private void deleteCustomFile(String fileName) {
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Files");
        File file = new File(directory, fileName + ".txt");

        if (file.exists()) {
            if (file.delete()) { // Удаление файла
                showToast("Файл удален"); // Вывод сообщения об успешном удалении файла
            } else {
                showToast("Ошибка при удалении файла"); // Вывод сообщения об ошибке при удалении файла
            }
        } else {
            showToast("Файл не найден"); // Вывод сообщения, если файл не найден
        }
    }

    // Вывод сообщений
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
