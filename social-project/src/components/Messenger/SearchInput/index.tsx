import React from "react";
import { Search } from "lucide-react";
import styles from "../index.module.css";

interface SearchInputProps {
  placeholder: string;
}

const SearchInput: React.FC<SearchInputProps> = ({ placeholder }) => {
  return (
    <div className={styles.searchWrapper}>
      <Search className={styles.searchIcon} />
      <input type="text" className={styles.searchInput} placeholder={placeholder} />
    </div>
  );
};

export default SearchInput;
