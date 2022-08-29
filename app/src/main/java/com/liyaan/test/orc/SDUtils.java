package com.liyaan.test.orc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
* ������ �� ���ڽ�RAW Ŀ¼�µ��ļ�д�뵽���ݿ���
* 
* @author Administrator
* 
*/
public class SDUtils {

        private String file; // �����ļ����·��
        private String fileName; // ����ļ�����
        private Context context; // ��ȡ��Context ������
        private int rawid; // ��Դ�ļ�ID ����ҪCOPY ���ļ�
        private String DATABASE_PATH = "";
        private String DATABASE_NAME = "";

        public String getFile() {
                return file;
        }

        public void setFile(String file) {
                this.file = file;
                this.DATABASE_PATH = Environment.getExternalStorageDirectory()
                                .getAbsolutePath() + "/" + file;
        }

        public String getFileName() {
                return fileName;
        }

        public void setFileName(String fileName) {
                this.fileName = fileName;
                this.DATABASE_NAME = fileName;
        }

        public int getRawid() {
                return rawid;
        }

        public void setRawid(int rawid) {
                this.rawid = rawid;
        }

        public SDUtils() {
        }

        /**
         * 
         * @param file
         *            �ļ������磺 aa/bb
         * @param fileName
         *            �ļ���
         * @param context
         *            ������
         * @param rawid
         *            ��ԴID
         */
        public SDUtils(String file, String fileName, Context context, int rawid) {
                super();
                this.file = file;
                this.fileName = fileName;
                this.context = context;
                this.rawid = rawid;
                this.DATABASE_PATH = Environment.getExternalStorageDirectory()
                                .getAbsolutePath() + "/" + file;
                this.DATABASE_NAME = fileName;
        }

        /**
         * ���ļ����Ƶ�SD���������ظ��ļ���Ӧ�����ݿ����
         * 
         * @return
         * @throws IOException
         */
        public boolean getSQLiteDatabase() throws IOException {

                // �����жϸ�Ŀ¼�µ��ļ����Ƿ����
                File dir = new File(DATABASE_PATH);
                String filename1 = DATABASE_PATH + "/" + DATABASE_NAME;
                if (!dir.exists()) {
                        // �ļ��в����� �� �򴴽��ļ���
                        dir.mkdirs();
                }

                // �ж�Ŀ���ļ��Ƿ����
                File file1 = new File(dir, DATABASE_NAME);

                if (!file1.exists()) {
                        Log.i("msg", "û���ļ�����ʼ����");
                        file1.createNewFile(); // �����ļ�

                }

                Log.i("msg", "׼����ʼ�����ļ��ĸ���");
                // ��ʼ�����ļ��ĸ���
                InputStream input = context.getResources().openRawResource(rawid); // ��ȡ��Դ�ļ�raw
                                                                                                                                                        // ���
                try {

                        FileOutputStream out = new FileOutputStream(file1); // �ļ�����������ڽ��ļ�д��SD����
                                                                                                                                // -- ���ڴ��ȥ
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = (input.read(buffer))) != -1) { // ��ȡ�ļ���-- �����ڴ�

                                out.write(buffer, 0, len); // д������ ��-- ���ڴ��
                        }

                        input.close();
                        out.close(); // �ر���

//                        SQLiteDatabase sqlitDatabase = SQLiteDatabase.openOrCreateDatabase(
//                                        filename1, null);
                        return true;
                } catch (Exception e) {
                	Log.i("msg", "�����쳣");
                	return false;
                }

                

        }

}

