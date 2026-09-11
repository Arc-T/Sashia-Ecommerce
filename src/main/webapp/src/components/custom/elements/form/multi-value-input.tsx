import { useState } from "react";
import { useTranslation } from "react-i18next";
import { Plus, X } from "lucide-react";
import { type Control, useFieldArray } from "react-hook-form";

import { Button } from "@/components/ui/button";
import { Field } from "@/components/ui/field";
import { Input } from "@/components/ui/input";

import { FieldHeader } from "@/components/custom/elements/form/field-header";
import { type CreateAttributeFormValues } from "@/types/attribute";

interface RHFMultiValueInputProps {
  control: Control<CreateAttributeFormValues>;
  name: string;
  label?: string;
  description?: string;
  placeholder?: string;
  required?: boolean;

  icon?: React.ReactNode;

  addButtonText?: string;
  allowDuplicates?: boolean;
  maxItems?: number;
}

export function RHFMultiValueInput({
  control,
  label,
  description,
  placeholder,
  required,
  icon,
  addButtonText = "add",
  allowDuplicates = false,
  maxItems,
  name,
}: RHFMultiValueInputProps) {
  const { t, i18n } = useTranslation();

  const [inputValue, setInputValue] = useState("");

  const { fields, append, remove } = useFieldArray({
    control,
    name: "values",
  });

  const addValue = () => {
    const value = inputValue.trim();

    if (!value) return;

    if (
      !allowDuplicates &&
      fields.some((field) => field.value.toLowerCase() === value.toLowerCase())
    ) {
      return;
    }

    if (maxItems && fields.length >= maxItems) {
      return;
    }

    append({ value });
    setInputValue("");
  };

  return (
    <Field>
      <FieldHeader
        label={label}
        description={description}
        required={required}
        icon={icon}
      />

      <div className="flex gap-2">
        <Input
          name={name}
          dir={i18n.dir()}
          value={inputValue}
          placeholder={placeholder ? t(placeholder) : undefined}
          onChange={(e) => setInputValue(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === "Enter") {
              e.preventDefault();
              addValue();
            }
          }}
        />

        <Button type="button" onClick={addValue} disabled={!inputValue.trim()}>
          <Plus className="size-4" />
          {t(addButtonText)}
        </Button>
      </div>

      {fields.length > 0 && (
        <div className="flex flex-wrap gap-2 rounded-lg border border-border bg-muted/30 p-3">
          {fields.map((field, index) => (
            <div
              key={field.id}
              className="inline-flex items-center gap-1.5 rounded-full bg-primary/10 px-3 py-1.5 text-sm text-primary"
            >
              <span>{field.value}</span>

              <button
                type="button"
                onClick={() => remove(index)}
                className="rounded-full p-0.5 transition-colors hover:bg-primary/20"
                aria-label={t("remove")}
              >
                <X className="size-3" />
              </button>
            </div>
          ))}
        </div>
      )}
    </Field>
  );
}
