DB_NAME="student_api"
DB_USER="ukwksk"
PGPASSWORD="ukwksk_pwd"

copy_csv() {
  csv="/tmp/db/csv/${1}"
  t=$(basename ${csv#*_})
  table=${t%.*}

  columns=$(head -n 1 $csv)
  echo $table $columns
  psql -e --username ${DB_USER} ${DB_NAME} \
    -c "\copy public.${table}($columns) from '${csv}' with csv header"
}

copy_csv 1_teacher.csv
copy_csv 2_classroom.csv
copy_csv 3_student.csv
