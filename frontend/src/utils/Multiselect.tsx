import React, { useState } from 'react';
import { Dropdown, Form } from 'react-bootstrap';

interface Option {
  id: number;
  label: string;
}

interface MultiselectProps {
  options: Option[];
  onSelect: (selectedOptions: Option[]) => void;
}

const Multiselect: React.FC<MultiselectProps> = ({ options, onSelect }) => {
  const [selectedOptions, setSelectedOptions] = useState<Option[]>([]);

  const handleToggle = (option: Option) => {
    const isSelected = selectedOptions.some((selected) => selected.id === option.id);
    const newSelectedOptions = isSelected
      ? selectedOptions.filter((selected) => selected.id !== option.id)
      : [...selectedOptions, option];

    setSelectedOptions(newSelectedOptions);
    onSelect(newSelectedOptions);
  };



  return (
    <Dropdown autoClose="outside" drop="down">
      <Dropdown.Toggle variant="primary" id="multiselect-dropdown">
        {selectedOptions.length > 0
          ? `${selectedOptions.length} item(s) selected`
          : 'Select options'}
      </Dropdown.Toggle>

      <Dropdown.Menu>
        {options.map((option) => (
          <Dropdown.Item as="div" key={option.id}>
            <Form.Check
              type="checkbox"
              label={option.label}
              checked={selectedOptions.some((selected) => selected.id === option.id)}
              onChange={() => handleToggle(option)} 
              id={`checkbox-${option.id}`} 
            />
          </Dropdown.Item>
        ))}
      </Dropdown.Menu>
    </Dropdown>
  );
};

export default Multiselect;
