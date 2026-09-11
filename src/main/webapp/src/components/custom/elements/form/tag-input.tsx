import { useState } from "react";
import { type Control, useFieldArray } from "react-hook-form";
import { Plus, X } from "lucide-react";

import { RHFInput } from "@/components/custom/elements/form/input";
import { Field, FieldGroup } from "@/components/ui/field";
import { Button } from "@/components/ui/button";

import { type CreateAttributeFormValues } from "@/types/attribute";

type Props = {
  control: Control<CreateAttributeFormValues>;
  label: string;
  description?: string;
  placeholder?: string;
  icon?: React.ReactNode;
};

export function RHFTagInput({
  control,
  label,
  description,
  placeholder,
  icon,
}: Props) {
  const [input, setInput] = useState("");

  const { fields, append, remove } = useFieldArray({
    control,
    name: "values",
  });

  const add = () => {
    const value = input.trim();

    if (!value) return;

    if (fields.some((item) => item.value.toLowerCase() === value.toLowerCase()))
      return;

    append({ value });
    setInput("");
  };

  return (
    <FieldGroup>
      <Field label={label} description={description} icon={icon}>
        <div className="flex gap-2">
          <RHFInput
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder={placeholder}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                e.preventDefault();
                add();
              }
            }}
          />

          <Button
            type="button"
            onClick={add}
            disabled={!input.trim()}
            size="icon"
          >
            <Plus className="size-4" />
          </Button>
        </div>

        {fields.length > 0 && (
          <div className="mt-3 flex flex-wrap gap-2">
            {fields.map((field, index) => (
              <Button
                key={field.id}
                type="button"
                variant="secondary"
                size="sm"
                className="gap-1"
                onClick={() => remove(index)}
              >
                {field.value}
                <X className="size-3" />
              </Button>
            ))}
          </div>
        )}
      </Field>
    </FieldGroup>
  );
}
